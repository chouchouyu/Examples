package com.cm.android.skin.core;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import org.w3c.dom.Text;

public class SkinResources {

    private Resources mAppResources;
    private Resources mSkinResources;
    private String mSkinPkgName;
    private boolean isDefaultSkin = true;

    public SkinResources(Context context) {
        mAppResources = context.getResources();
    }

    public static SkinResources getInstance() {
        return instance;
    }

    private static SkinResources instance;

    public static void init(Context context) {
        if (instance == null) {
            synchronized (SkinResources.class) {
                if (instance == null) {
                    instance = new SkinResources(context);
                }
            }
        }
    }

    public void reset() {
        mSkinResources = null;
        mSkinPkgName = "";
        isDefaultSkin = true;
    }

    public void applySkin(Resources skinResource, String packageName) {
        mSkinResources = skinResource;
        mSkinPkgName = packageName;
        //是否使用默认皮肤
        isDefaultSkin = TextUtils.isEmpty(packageName) || skinResource == null;
    }

    public int getIdentifier(int resId) {
        if (isDefaultSkin) {
            return resId;
        }
        //在皮肤包中不一定是 当前程序的 id
        //获取对应id 在当前的名称 colorPrimary
        //R.drawable.ic_launcher
        String resName = mAppResources.getResourceEntryName(resId);//ic_launcher
        String resType = mAppResources.getResourceTypeName(resId);//drawable
        int skinId = mSkinResources.getIdentifier(resName, resType, mSkinPkgName);
        return skinId;
    }

    public int getColor(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColor(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColor(resId);
        }
        return mSkinResources.getColor(skinId);
    }

    public ColorStateList getColorStateList(int resId) {
        if (isDefaultSkin) {
            return mAppResources.getColorStateList(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getColorStateList(resId);
        }
        return mSkinResources.getColorStateList(skinId);
    }

    public Drawable getDrawable(int resId) {
        //如果有皮肤 isDefaultSkin false没有就是true
        if (isDefaultSkin) {
            return mAppResources.getDrawable(resId);
        }
        int skinId = getIdentifier(resId);
        if (skinId == 0) {
            return mAppResources.getDrawable(resId);
        }
        return mSkinResources.getDrawable(skinId);
    }

    /**
     * 可能是Color 也可能是drawable
     *
     * @param resId
     * @return
     */
    public Object getBackground(int resId) {
        String resourceTypeName = mAppResources.getResourceTypeName(resId);

        if (resourceTypeName.equals("color")) {
            return getColor(resId);
        } else {
            //drawable
            return getDrawable(resId);
        }
    }

    public String getString(int resId) {
        try {
            if (isDefaultSkin) {
                return mAppResources.getString(resId);
            }
            int skinId = getIdentifier(resId);
            if (skinId == 0) {
                return mAppResources.getString(skinId);
            }
            return mSkinResources.getString(skinId);
        } catch (Resources.NotFoundException e) {

        }
        return null;
    }

    public Typeface getTypeface(int resId) {
        String skinTypefacePath = getString(resId);
        if (TextUtils.isEmpty(skinTypefacePath)) {
            return Typeface.DEFAULT;
        }
        try {
            Typeface typeface;
            if (isDefaultSkin) {
                typeface = Typeface.createFromAsset(mAppResources.getAssets(), skinTypefacePath);
                return typeface;
            }
            typeface = Typeface.createFromAsset(mSkinResources.getAssets(), skinTypefacePath);
            return typeface;
        } catch (RuntimeException e) {

        }
        return Typeface.DEFAULT;
    }
}
