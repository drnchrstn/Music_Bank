package com.example.musicbank.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DiskLruCacheUtil {

    private DiskLruCache mDiskLurCache;
    private final long CACHE_MAXSIZE = 10 * 1024 * 1024;


    public DiskLruCacheUtil(Context mContext, String fileName) {
        try {
            if (mDiskLurCache != null) {
                mDiskLurCache.close();
                mDiskLurCache = null;
            }
            int appVersionCode = getAppVersionCode(mContext);


            mDiskLurCache = DiskLruCache.open(mContext.getExternalCacheDir(), appVersionCode, 1, CACHE_MAXSIZE);
        } catch (IOException e) {


        }

    }

    public DiskLruCache.Editor editor(String key) {
        if (mDiskLurCache != null) {
            try {
                String md5Key = getMD5(key);
                DiskLruCache.Editor edit = mDiskLurCache.edit(md5Key);
                return edit;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void put(String key, byte[] bytes) {
        OutputStream out = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = editor(key);
            if (editor == null) {
                return;
            }
            out = editor.newOutputStream(0);
            out.write(bytes);
            out.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {

            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void putObject(String key, Object object) {
        ObjectOutputStream oos = null;
        DiskLruCache.Editor editor = null;
        OutputStream outputStream = null;
        try {
            editor = editor(key);
            if (editor == null) {
                return;
            }
            outputStream = editor.newOutputStream(0);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(object);
            oos.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null)
                    editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {


            }
        }
    }



    public DiskLruCache.Snapshot snapshot(String key) {

        String md5Key = getMD5(key);
        if (mDiskLurCache != null) {
            try {
                DiskLruCache.Snapshot snapshot = mDiskLurCache.get(md5Key);
                return snapshot;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void put(String key, String value) {
        DiskLruCache.Editor editor = null;
        BufferedWriter writer = null;
        OutputStream os = null;
        try {
            editor = editor(key);
            if (editor == null) {
                return;
            }
            os = editor.newOutputStream(0);
            writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write(value);
            writer.flush();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (editor != null)
                    editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getMD5(String str) {
        MessageDigest messageDigest = null;

        try {

            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    private int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public String getStringCache(String key) {
        InputStream inputStream = getInputStream(key);
        InputStreamReader inputStreamReader = null;
        BufferedReader in = null;
        if (inputStream == null) {
            return null;
        }

        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            in = new BufferedReader(inputStreamReader);
            StringBuilder buffer = new StringBuilder();
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    private InputStream getInputStream(String key) {
        DiskLruCache.Snapshot snapshot = snapshot(key);
        if (snapshot == null) {
            return null;
        }
        InputStream inputStream = snapshot.getInputStream(0);
        return inputStream;
    }


    public <T> T getObjectCache(String key) {
        InputStream inputStream = getInputStream(key);
        ObjectInputStream ois = null;
        T object = null;
        if (inputStream == null) {
            return null;
        }
        try {
            ois = new ObjectInputStream(inputStream);
            object = (T) ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {

            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }





}
