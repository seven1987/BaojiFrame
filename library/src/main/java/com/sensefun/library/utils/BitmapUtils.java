package com.sensefun.library.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

/**
 * Bitmap Utils
 * <p/>
 * Created by Jinjia on 16/6/24.
 */
public final class BitmapUtils {

    public static boolean isTooLarge(Context context, Uri uri, int width, int height) {
        if (context == null || uri == null) {
            return false;
        }

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            if (is != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);
                return options.outWidth > width || options.outHeight > height;
            }
        } catch (FileNotFoundException e) {
            Timber.e(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }

        return false;
    }

    public static BitmapFactory.Options getOptions(Context context, Uri uri, int width, int height) {
        if (context == null || uri == null) {
            return null;
        }

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            if (is != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);

                int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
                int widthRatio = (int) Math.ceil(options.outWidth / (float) width);
                if (heightRatio > 1 || widthRatio > 1) {
                    options.inSampleSize = Math.max(widthRatio, heightRatio);
                } else {
                    options.inSampleSize = 1;
                }
                options.inJustDecodeBounds = false;
                return options;
            }
        } catch (FileNotFoundException e) {
            Timber.e(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }

        return null;
    }

    private static Bitmap shrinkBitmap(Context context, Uri uri, int width, int height) {
        BitmapFactory.Options options = getOptions(context, uri, width, height);
        if (options == null) {
            return null;
        }

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            Timber.e(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
        return null;
    }

    public static byte[] shrinkBitmap(Context context, Uri uri, int width, int height, int maxSize) {
        Bitmap bitmap = shrinkBitmap(context, uri, width, height);
        if (bitmap == null) {
            return null;
        }

        return compressImage(bitmap, maxSize);
    }

    public static byte[] compressImage(Bitmap bitmap, int maxSize) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int options = 100;
        do {
            os.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);
            options -= 10;
        } while (os.toByteArray().length > maxSize);
        return os.toByteArray();
    }

    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                             int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }

        return inSampleSize;
    }

    private static int calculateInSampleSize(Context context, Uri uri, int width, int height) {
        BitmapFactory.Options originOption = new BitmapFactory.Options();
        originOption.inJustDecodeBounds = true;

        InputStream is = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            is = resolver.openInputStream(uri);
            BitmapFactory.decodeStream(is, null, originOption);
            return calculateInSampleSize(originOption, width, height);
        } catch (FileNotFoundException e) {
            Timber.e(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }

        return 1;
    }

    public static Bitmap createScaleBitmap(Context context, Uri uri, int width, int height) {
        InputStream is = null;
        try {
            BitmapFactory.Options destOption = new BitmapFactory.Options();
            destOption.inJustDecodeBounds = false;
            destOption.inSampleSize = calculateInSampleSize(context, uri, width, height);
            destOption.inPreferredConfig = Bitmap.Config.RGB_565;
            is = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(is, null, destOption);
        } catch (FileNotFoundException e) {
            Timber.e(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }

        return null;
    }

}