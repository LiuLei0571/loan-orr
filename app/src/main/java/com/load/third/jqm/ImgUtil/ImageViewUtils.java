package com.load.third.jqm.ImgUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.load.third.jqm.utils.StringUtils;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public final class ImageViewUtils {


	public static void displayImage(Context context, String url,
                                    ImageView imageView) {
		if (StringUtils.isBlank(url)) {
			return;
		}
		Uri uri;
		if (url.startsWith("http")) {
			uri = Uri.parse(url);
		} else {
			if (url.startsWith("file://"))
				url = url.substring(7);
			try {
				Bitmap bitmap = HHImageUtils.getImageBitmap(url, 920,
						1632 * 920);
				int angle = HHImageUtils.getExifOrientation(url);
				// 如果照片出现了旋转那�? 就更改旋转度�?
				if (angle != 0) {
					Matrix matrix = new Matrix();
					matrix.postRotate(angle);
					bitmap = Bitmap
							.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
									bitmap.getHeight(), matrix, true);
				}
				imageView.setImageBitmap(bitmap);
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
			uri = Uri.fromFile(new File(url));
		}
		Glide.with(context).load(uri).into(imageView);
	}


	public static void displayCircleImage(final Context context, String url,
                                          final ImageView imageView) {
		Glide.with(context).load(url)
				.bitmapTransform(new CropCircleTransformation(context))
				.into(imageView);

	}

	public static void displayBlurImage(Context context, String url, final ImageView imageView){
		Glide.with(context).load(url)
				.bitmapTransform(new BlurTransformation(context,75))
				.into(imageView);
	}

	public static void displayRoundedImage(final Context context, String url,
                                           final ImageView imageView, int cornerSize) {

		Glide.with(context).load(url).transform(new GlideRoundTransform(context,cornerSize)).into(imageView);
	}

	public static void displayRoundedImage(final Context context, String url,
                                           final ImageView imageView) {

		Glide.with(context).load(url).transform(new GlideRoundTransform(context)).into(imageView);
	}

}
