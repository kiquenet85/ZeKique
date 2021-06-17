package com.example.zemogatest.util;

import android.content.Context;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * @author n.diazgranados
 */
public class FileUtil {

    public static Reader readFile(Context context, int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    }
}
