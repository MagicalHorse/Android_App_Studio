package com.shenma.yueba.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.shenma.yueba.constants.Constants;

public class PhotoUtils {
	// 图片在SD卡中的缓存路径
	private static final String IMAGE_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ File.separator
			+ "GameChating" + File.separator + "Images" + File.separator;
	// 相册的RequestCode
	public static final int INTENT_REQUEST_CODE_ALBUM = 0;
	// 照相的RequestCode
	public static final int INTENT_REQUEST_CODE_CAMERA = 1;
	// 裁剪照片的RequestCode
	public static final int INTENT_REQUEST_CODE_CROP = 2;
	// 滤镜图片的RequestCode
	public static final int INTENT_REQUEST_CODE_FLITER = 3;
	public static final String IMAGE_UNSPECIFIED = "image/*";

	/**
	 * 通过手机相册获取图片
	 *
	 * @param obj Object
	 */
	public static void selectPhoto(Object obj) {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		if(obj instanceof Activity)
		{
			((Activity)obj).startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
		}else if(obj instanceof Fragment)
		{
			((Fragment)obj).startActivityForResult(intent, INTENT_REQUEST_CODE_ALBUM);
		}
	}

	/**
	 * 通过手机照相获取图片
	 *
	 * @param obj Object
	 * @return 照相后图片的路径
	 */
	public static String takePicture(Object obj) {
		FileUtils.createDirFile(IMAGE_PATH);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String path = IMAGE_PATH + UUID.randomUUID().toString() + "jpg";
		File file = FileUtils.createNewFile(path);
		if (file != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		}
		if(obj instanceof Activity)
		{
			((Activity)obj).startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		}else if(obj instanceof Fragment)
		{
			((Fragment)obj).startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
		}
		return path;
	}

	public static Bitmap getBitmapFromLocal(String path) {
		Bitmap bitmap = null;
		try {
			FileInputStream fin = new FileInputStream(new File(path));
			// 做过缩放处理的bitmap
			bitmap = BitmapFactory
					.decodeFileDescriptor(fin.getFD(), null, null);
			if (fin != null) {
				fin.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            需要裁剪的图片路径
	 */
	public static Intent getZoomIntent(Uri uri, Uri uri_cache) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("aspectX", 1);// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectY",1);;
		intent.putExtra("outputX", 800);// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputY", 800);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("scale", false);// 黑边
		intent.putExtra("scaleUpIfNeeded", false);// 黑边
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_cache);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("return-data", false);
		return intent;
	}
	
	
	/**
	 * 裁剪图片
	 * 
	 * @param context
	 * @param activity
	 * @param path
	 *            需要裁剪的图片路径
	 */
	public static Intent getZoomIntent(Uri uri, Uri uri_cache,int aspectX,int aspectY) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("aspectX", 8);// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectY",5);
		intent.putExtra("outputX", 800);// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputY", 500);
		intent.putExtra("scale", true);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("scale", false);// 黑边
		intent.putExtra("scaleUpIfNeeded", false);// 黑边
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_cache);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("return-data", false);
		return intent;
	}
	
//
//	/**
//	 * 裁剪图片
//	 * 
//	 * @param context
//	 * @param activity
//	 * @param path
//	 *            需要裁剪的图片路径
//	 */
//	public static Intent getZoomIntent(Uri uri, Uri uri_cache) {
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//		intent.putExtra("aspectX", 1);// aspectX aspectY 是宽高的比例
//		intent.putExtra("aspectY",1.6);;
//		intent.putExtra("outputX", ToolsUtil.getDisplayWidth(cx));// outputX outputY 是裁剪图片宽高
//		intent.putExtra("outputY", 480);
//		intent.putExtra("scale", true);
//		intent.putExtra("scaleUpIfNeeded", true);
//		intent.putExtra("scale", false);// 黑边
//		intent.putExtra("scaleUpIfNeeded", false);// 黑边
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri_cache);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		// intent.putExtra("noFaceDetection", true); // no face detection
//		intent.putExtra("return-data", false);
//		return intent;
//	}
//
//	
	/**
	 * 删除图片缓存目录
	 */
	public static void deleteImageFile() {
		File dir = new File(IMAGE_PATH);
		if (dir.exists()) {
			FileUtils.delFolder(IMAGE_PATH);
		}
	}

	/**
	 * 从文件中获取图片
	 * 
	 * @param path
	 *            图片的路径
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 从Uri中获取图片
	 * 
	 * @param cr
	 *            ContentResolver对象
	 * @param uri
	 *            图片的Uri
	 * @return
	 */
	public static Bitmap getBitmapFromUri(ContentResolver cr, Uri uri) {
		try {
			return BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	public static BitmapFactory.Options getOptions(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
		BitmapFactory.decodeFile(path, opts);
		int srcWidth = opts.outWidth;// 获取图片的原始宽度
		int srcHeight = opts.outHeight;// 获取图片原始高度
		int destWidth = 0;
		int destHeight = 0;
		// 缩放的比例
		double ratio = 0.0;
		if (srcWidth < w || srcHeight < h) {
			ratio = 0.0;
			destWidth = srcWidth;
			destHeight = srcHeight;
		} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
			ratio = (double) srcWidth / w;
			destWidth = w;
			destHeight = (int) (srcHeight / ratio);
		} else {
			ratio = (double) srcHeight / h;
			destHeight = h;
			destWidth = (int) (srcWidth / ratio);
		}
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		newOpts.inSampleSize = (int) ratio + 1;
		// inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
		newOpts.outHeight = destHeight;
		newOpts.outWidth = destWidth;

		newOpts.inDither = false;
		newOpts.inPurgeable = true;
		newOpts.inTempStorage = new byte[12 * 1024];

		return newOpts;
	}

	public static BitmapFactory.Options getOptions2(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
		BitmapFactory.decodeFile(path, opts);
		final int REQUIRED_SIZE = 70;
		int width_tmp = opts.outWidth, height_tmp = opts.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
		newOpts.inSampleSize = scale;
		// inJustDecodeBounds设为false表示把图片读进内存中
		newOpts.inJustDecodeBounds = false;
		// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置不能缩放
		// newOpts.outHeight = destHeight;
		// newOpts.outWidth = destWidth;

		newOpts.inDither = false;
		newOpts.inPurgeable = true;
		newOpts.inTempStorage = new byte[12 * 1024];

		return newOpts;
	}

	/**
	 * 根据宽度和长度进行缩放图片
	 * 
	 */
	public static Bitmap createBitmap(String path, String path2, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			newOpts.inDither = false;
			newOpts.inPurgeable = true;
			newOpts.inTempStorage = new byte[12 * 1024];

			// 这种方式获得Bitmap不容易内存溢出
			FileDescriptor fd = new FileInputStream(new File(path)).getFD();
			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd, null,
					newOpts);

			// 把图片的质量压缩一下并保存到path2的路径上
			ToolsUtil.saveBitmap(bitmap, path2);

			// 返回path2路径上的Bitmap
			return BitmapFactory.decodeFileDescriptor(new FileInputStream(
					new File(path2)).getFD(), null, null);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/***
	 * 通过图片的路径得到Bitmap
	 * 
	 * @param path
	 * @return
	 */
	public static void WriteBitmapToLocal(String path, String path2, int w,
			int h) {
		// 这种方式获得Bitmap不容易内存溢出
		try {
			String dir1 = path.substring(0, path.lastIndexOf("/") + 1);
			String name1 = path.substring(path.lastIndexOf("/") + 1,
					path.length());
			String dir2 = path2.substring(0, path2.lastIndexOf("/") + 1);
			String name2 = path2.substring(path2.lastIndexOf("/") + 1,
					path2.length());

			File dirfile1 = new File(dir1);
			File dirfile2 = new File(dir2);
			if (!dirfile1.exists()) {
				dirfile1.mkdirs();
			}
			if (!dirfile2.exists()) {
				dirfile2.mkdirs();
			}

			File file1 = new File(path);
			if (!file1.exists()) {
				file1.createNewFile();
			}
			File file2 = new File(path2);
			if (!file2.exists()) {
				file2.createNewFile();
			}

			FileInputStream fin = new FileInputStream(file1);
			// 做过缩放处理的bitmap
			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fin.getFD(),
					null, getOptions(path, w, h));

			// 对得到的bitmap进行质量的压缩，存入本地
			FileOutputStream fos = new FileOutputStream(file2);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);

			// 不在使用Bitmap，需要释放内存
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
			if (fin != null) {
				fin.close();
			}
			if (fos != null) {
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取bitmap尺寸缩放解决OOM问题
	 * 
	 * @throws IOException
	 */
	private Bitmap convertBitmap(File file) throws IOException {
		Bitmap bitmap = null;
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		BitmapFactory.decodeStream(fis, null, o);
		fis.close();
		final int REQUIRED_SIZE = 70;
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inSampleSize = scale;
		fis = new FileInputStream(file.getAbsolutePath());
		bitmap = BitmapFactory.decodeStream(fis, null, op);
		fis.close();
		return bitmap;
	}

	/**
	 * 获取图片的长度和宽度
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static Bundle getBitmapWidthAndHeight(Bitmap bitmap) {
		Bundle bundle = null;
		if (bitmap != null) {
			bundle = new Bundle();
			bundle.putInt("width", bitmap.getWidth());
			bundle.putInt("height", bitmap.getHeight());
			return bundle;
		}
		return null;
	}

	/**
	 * 判断图片高度和宽度是否过大
	 * 
	 * @param bitmap
	 *            图片bitmap对象
	 * @return
	 */
	public static boolean bitmapIsLarge(Bitmap bitmap) {
		final int MAX_WIDTH = 60;
		final int MAX_HEIGHT = 60;
		Bundle bundle = getBitmapWidthAndHeight(bitmap);
		if (bundle != null) {
			int width = bundle.getInt("width");
			int height = bundle.getInt("height");
			if (width > MAX_WIDTH && height > MAX_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据比例缩放图片
	 * 
	 * @param screenWidth
	 *            手机屏幕的宽度
	 * @param filePath
	 *            图片的路径
	 * @param ratio
	 *            缩放比例
	 * @return
	 */
	public static Bitmap CompressionPhoto(int screenWidth, int screenHeight,
			String filePath, String filePath2, int ratio) {
		// Bitmap bitmap = PhotoUtils.getBitmapFromFile(filePath);
		Bitmap bitmap = PhotoUtils.createBitmap(filePath, filePath2,
				screenWidth, screenHeight);
		Bitmap compressionBitmap = null;
		float scaleWidth = screenWidth / (bitmap.getWidth() * ratio);
		float scaleHeight = screenWidth / (bitmap.getHeight() * ratio);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		try {
			compressionBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		} catch (Exception e) {
			return bitmap;
		}
		return compressionBitmap;
	}

	public static Bitmap getimage(int ww, int hh, String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		// float hh = 800f;//这里设置高度为800f
		// float ww = 480f;//这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public Bitmap comp(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param bitmap
	 *            图片的bitmap对象
	 * @return
	 */
	public static String savePhotoToSDCard(Bitmap bitmap) {
		if (!FileUtils.isSdcardExist()) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		FileUtils.createDirFile(Constants.SD);

		String fileName = UUID.randomUUID().toString() + ".jpg";
		String newFilePath = Constants.SD + fileName;
		File file = FileUtils.createNewFile(newFilePath);
		if (file == null) {
			return null;
		}
		try {
			fileOutputStream = new FileOutputStream(newFilePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
		} catch (FileNotFoundException e1) {
			return null;
		} finally {
			try {
				fileOutputStream.flush();
				fileOutputStream.close();
			} catch (IOException e) {
				return null;
			}
		}
		return newFilePath;
	}

	/**
	 * 滤镜效果--LOMO
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap lomoFilter(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int dst[] = new int[width * height];
		bitmap.getPixels(dst, 0, width, 0, 0, width, height);

		int ratio = width > height ? height * 32768 / width : width * 32768
				/ height;
		int cx = width >> 1;
		int cy = height >> 1;
		int max = cx * cx + cy * cy;
		int min = (int) (max * (1 - 0.8f));
		int diff = max - min;

		int ri, gi, bi;
		int dx, dy, distSq, v;

		int R, G, B;

		int value;
		int pos, pixColor;
		int newR, newG, newB;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = y * width + x;
				pixColor = dst[pos];
				R = Color.red(pixColor);
				G = Color.green(pixColor);
				B = Color.blue(pixColor);

				value = R < 128 ? R : 256 - R;
				newR = (value * value * value) / 64 / 256;
				newR = (R < 128 ? newR : 255 - newR);

				value = G < 128 ? G : 256 - G;
				newG = (value * value) / 128;
				newG = (G < 128 ? newG : 255 - newG);

				newB = B / 2 + 0x25;

				// ==========边缘黑暗==============//
				dx = cx - x;
				dy = cy - y;
				if (width > height)
					dx = (dx * ratio) >> 15;
				else
					dy = (dy * ratio) >> 15;

				distSq = dx * dx + dy * dy;
				if (distSq > min) {
					v = ((max - distSq) << 8) / diff;
					v *= v;

					ri = (int) (newR * v) >> 16;
					gi = (int) (newG * v) >> 16;
					bi = (int) (newB * v) >> 16;

					newR = ri > 255 ? 255 : (ri < 0 ? 0 : ri);
					newG = gi > 255 ? 255 : (gi < 0 ? 0 : gi);
					newB = bi > 255 ? 255 : (bi < 0 ? 0 : bi);
				}
				// ==========边缘黑暗end==============//

				dst[pos] = Color.rgb(newR, newG, newB);
			}
		}

		Bitmap acrossFlushBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		acrossFlushBitmap.setPixels(dst, 0, width, 0, 0, width, height);
		return acrossFlushBitmap;
	}

	/**
	 * @return 返回指定笔和指定字符串的长度
	 */
	public static float getFontlength(Paint paint, String str) {
		return paint.measureText(str);
	}

	/**
	 * @return 返回指定笔的文字高度
	 */
	public static float getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.descent - fm.ascent;
	}

	/**
	 * @return 返回指定笔离文字顶部的基准距离
	 */
	public static float getFontLeading(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return fm.leading - fm.ascent;
	}

	public static Drawable getImageDrawable(String file) {
		if (!FileUtils.exists(file))
			return null;
		try {
			InputStream inp = new FileInputStream(new File(file));
			return BitmapDrawable.createFromStream(inp, "img");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取圆角图片
	 * 
	 * @param bitmap
	 * @param pixels
	 * @return
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 获取颜色的圆角bitmap
	 * 
	 * @param context
	 * @param color
	 * @return
	 */
	public static Bitmap getRoundBitmap(Context context, int color) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 12.0f, metrics));
		int height = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4.0f, metrics));
		int round = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 2.0f, metrics));
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawRoundRect(new RectF(0.0F, 0.0F, width, height), round,
				round, paint);
		return bitmap;
	}
}
