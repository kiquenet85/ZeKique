package com.example.zemogatest.common.manager

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.zemogatest.util.EMPTY_STRING
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Resource Manager Singleton
 * @author n.diazgranados
 *
 * This class is responsible to search any resource and also all logic related for doing it.
 *
 * Context could be conveniently used with an specific context, otherwise will use application context.
 */
class ResourceManager @Inject constructor(@ApplicationContext private val appContext: Context) {

    private val simpleDate: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val formatCOP = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 0
        currency = Currency.getInstance("COP")
    }

    enum class ResourceType(val value: String) {
        STRING("string"),
        ARRAY("array"),
        LAYOUT("layout"),
        ID("id")
    }

    fun getResources(context: Context = appContext) =
        context.resources

    fun getIdentifier(
        resourceType: ResourceType,
        identifierName: String,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        try {
            getResources(context).getIdentifier(
                identifierName,
                resourceType.value,
                context.packageName
            )
        } catch (e: Exception) {
            defaultValue
        }

    @JvmOverloads
    fun getStringArray(
        @ArrayRes idArrayRes: Int,
        defaultValue: String = RESOURCE_NOT_FOUND,
        context: Context = appContext
    ) =
        try {
            getResources(context).getStringArray(idArrayRes).toList()
        } catch (e: Exception) {
            listOf(defaultValue)
        }


    @JvmOverloads
    fun getString(
        @StringRes idStringRes: Int,
        defaultValue: String = RESOURCE_NOT_FOUND,
        context: Context = appContext
    ) =
        try {
            getResources(context).getString(idStringRes)
        } catch (e: Exception) {
            defaultValue
        }

    @JvmOverloads
    fun getDrawable(
        @DrawableRes idDrawableRes: Int?,
        @DrawableRes defaultValue: Int = android.R.drawable.ic_input_add,
        context: Context = appContext
    ) =
        try {
            idDrawableRes?.let {
                ResourcesCompat.getDrawable(
                    getResources(context),
                    idDrawableRes,
                    null
                )
            }
                ?: ResourcesCompat.getDrawable(getResources(context), defaultValue, null)
        } catch (e: Exception) {
            ResourcesCompat.getDrawable(
                getResources(context),
                android.R.drawable.stat_notify_error,
                null
            )
        }

    fun getStringWithParams(
        @StringRes idStringRes: Int,
        vararg params: Any
    ) =
        try {
            getResources(appContext).getString(idStringRes, *params)
        } catch (e: Exception) {
            RESOURCE_NOT_FOUND
        }

    @JvmOverloads
    fun getColor(
        @ColorRes idColorRes: Int,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        try {
            ContextCompat.getColor(context, idColorRes)
        } catch (e: Exception) {
            defaultValue
        }

    @JvmOverloads
    fun getDimension(
        @DimenRes resourceId: Int,
        context: Context = appContext
    ) =
        getResources(context).getDimension(resourceId)

    @JvmOverloads
    fun getInteger(
        @IntegerRes resourceId: Int,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        try {
            getResources(context).getInteger(resourceId)
        } catch (e: Exception) {
            defaultValue
        }

    fun getQuantityString(
        @PluralsRes resourceId: Int,
        quantity: Int,
        arrayOfAny: Any
    ) =
        try {
            (appContext).resources.getQuantityString(resourceId, quantity, arrayOfAny)
        } catch (e: Exception) {
            RESOURCE_NOT_FOUND
        }

    @JvmOverloads
    fun getStringResourceId(
        identifierName: String,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        getIdentifier(ResourceType.STRING, identifierName, defaultValue, context)


    @JvmOverloads
    fun getArrayResourceId(
        identifierName: String,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        getIdentifier(ResourceType.ARRAY, identifierName, defaultValue, context)

    @JvmOverloads
    fun getLayoutResourceId(
        identifierName: String,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        getIdentifier(ResourceType.LAYOUT, identifierName, defaultValue, context)


    @JvmOverloads
    fun getViewResourceId(
        identifierName: String,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        getIdentifier(ResourceType.ID, identifierName, defaultValue, context)

    /**
     * Error codes managed in the hourly app are contained in String resources.
     * @return the id of the string resource
     */
    @JvmOverloads
    fun getErrorCodeDisplayMessage(
        errorCode: Int,
        defaultValue: String = RESOURCE_NOT_FOUND,
        context: Context = appContext
    ) =
        getString(getErrorMessageResId(errorCode, context = context), defaultValue, context)

    /**
     * Error codes managed in the hourly app are contained in String resources.
     * @return the id of the string resource
     */
    @JvmOverloads
    fun getErrorMessageResId(
        errorCode: Int,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        getStringResourceId(PREFIX_ERROR_CODE_IDENTIFIER.plus(errorCode), defaultValue, context)

    /**
     * Error codes managed in the hourly app are contained in String resources.
     * @return the id of the string resource
     */
    @JvmOverloads
    fun getErrorTitleResId(
        errorCode: Int,
        defaultValue: Int = NO_IDENTIFIER,
        context: Context = appContext
    ) =
        getStringResourceId(PREFIX_ERROR_TITLE_IDENTIFIER.plus(errorCode), defaultValue, context)


    /**
     * Formatting date
     */
    fun formatDate(date: Date): String {
        return simpleDate.format(date)
    }

    fun formatDate(dateString: String?): String {
        return dateString?.let { simpleDate.format(Date(dateString.toLong())) }
            ?: EMPTY_STRING
    }

    fun formatCurrencyCOP(currencyString: Long): String {
        return formatCOP.format(currencyString)
    }

    companion object {

        private const val PREFIX_ERROR_CODE_IDENTIFIER = "error_code_"
        private const val PREFIX_ERROR_TITLE_IDENTIFIER = "title_error_code_"
        private const val RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND"
        private const val NO_IDENTIFIER = -1

        @Volatile
        private var instance: ResourceManager? = null

        @JvmStatic
        fun getInstance(appContext: Context): ResourceManager {
            return instance ?: synchronized(this) {
                instance ?: ResourceManager(appContext).also { instance = it }
            }
        }
    }
}