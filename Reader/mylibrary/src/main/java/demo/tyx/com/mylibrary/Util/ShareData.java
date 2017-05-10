package demo.tyx.com.mylibrary.Util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ShareData
{
	public static int m_screenWidth = 0;
	public static int m_screenHeight = 0;
	public static int m_screenRealWidth = 0;
	public static int m_screenRealHeight = 0;
	public static int m_dpi = 0;
	public static float m_resScale = 0;

	private static boolean m_init = true;

	/**
	 * 初始化获取屏幕参数
	 *
	 * @param context
	 * @return 成功/失败
	 */
	public static boolean InitData(Context context)
	{
		boolean out = false;

		if(m_init)
		{
			if(context instanceof Activity)
			{
				m_init = false;

				Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
				DisplayMetrics metrics = new DisplayMetrics();
				display.getMetrics(metrics);
				m_dpi = metrics.densityDpi;
				m_screenWidth = metrics.widthPixels;
				m_screenHeight = metrics.heightPixels;
				if(m_screenWidth > m_screenHeight)
				{
					m_screenWidth += m_screenHeight;
					m_screenHeight = m_screenWidth - m_screenHeight;
					m_screenWidth -= m_screenHeight;
				}

				m_resScale = metrics.density;

				m_screenRealWidth = m_screenWidth;
				m_screenRealHeight = m_screenHeight;
				if(android.os.Build.VERSION.SDK_INT >= 17)
				{
					try
					{
						Method method = Display.class.getMethod("getRealMetrics", new Class[]{DisplayMetrics.class});
						method.invoke(display, new Object[]{metrics});
						m_screenRealWidth = metrics.widthPixels;
						m_screenRealHeight = metrics.heightPixels;
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
				out = true;
			}
		}
		else
		{
			out = true;
		}
		return out;
	}

	public static int getScreenW()
	{
		return m_screenWidth;
	}

	public static int getScreenH()
	{
		return m_screenHeight;
	}

	/**
	 * 素材以480x800屏幕为标准
	 *
	 * @param size
	 * @return
	 */
	public static int PxToDpi_hdpi(int size)
	{
		return (int)(size / 1.5f * m_resScale + 0.5f);
	}

	/**
	 * @param size
	 * @return
	 * @deprecated use {@link #PxToDpi_hdpi(int)}
	 */
	@Deprecated
	public static int PxToDpi(int size)
	{
		return PxToDpi_hdpi(size);
	}

	/**
	 * 素材以720*1280屏幕为标准
	 *
	 * @param size
	 * @return
	 */
	public static int PxToDpi_xhdpi(int size)
	{
		return (int)(size / 2f * m_resScale + 0.5f);
	}

	/**
	 * 素材以1080*1920屏幕为标准
	 *
	 * @param size
	 * @return
	 */
	public static int PxToDpi_xxhdpi(int size)
	{
		return (int)(size / 3f * m_resScale + 0.5f);
	}

	public static int getRealPixel_720P(int size)
	{
		return PxToDpi_xhdpi(size);
	}

	/**
	 * 获取状态栏高度(顶部),即使全屏也能获取
	 *
	 * @param ac
	 * @return
	 */
	public static int GetStatusBarHeight2(Activity ac)
	{
		int out = 0;
		try
		{
			if(ac != null)
			{
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object obj = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = Integer.parseInt(field.get(obj).toString());
				out = ac.getResources().getDimensionPixelSize(x);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return out;
	}

	/**
	 * 获取当前状态栏高度(顶部),全屏状态下返回0
	 *
	 * @param ac
	 * @return
	 */
	public static int GetCurrentStatusBarHeight(Activity ac)
	{
		int out = 0;

		//非全屏,有状态栏
		if(ac != null && (ac.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0)
		{
			out = GetStatusBarHeight2(ac);
		}

		return out;
	}

	/**
	 * 获取NavigationBar的高度
	 */
	public static int getNavigationBarHeight(Context context)
	{
		int navigationBarHeight = 0;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
		if(id > 0 && checkDeviceHasNavigationBar(context))
		{
			navigationBarHeight = rs.getDimensionPixelSize(id);
		}
		return navigationBarHeight;
	}

	/**
	 * 判断是否存在NavigationBar
	 *
	 * @param context
	 * @return
	 */
	public static boolean checkDeviceHasNavigationBar(Context context)
	{
		boolean hasNavigationBar = false;
		Resources rs = context.getResources();
		int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
		if(id > 0)
		{
			hasNavigationBar = rs.getBoolean(id);
		}
		try
		{
			Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
			Method m = systemPropertiesClass.getMethod("get", String.class);
			String navBarOverride = (String)m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
			if("1".equals(navBarOverride))
			{
				hasNavigationBar = false;
			}
			else if("0".equals(navBarOverride))
			{
				hasNavigationBar = true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hasNavigationBar;
	}

	/**
	 * 是否有物理Home键，返回true则有，否则为false
	 */
	public static boolean hasPhysicalKey(Context context)
	{
		boolean hasPermanentMenuKey = false;
		if(Build.VERSION.SDK_INT >= 14)
		{
			ViewConfiguration vc = ViewConfiguration.get(context);
			try
			{
				Method m = ViewConfiguration.get(context).getClass().getMethod("hasPermanentMenuKey");
				hasPermanentMenuKey = (Boolean)m.invoke(vc);
			}
			catch(Throwable e)
			{
				e.printStackTrace();
				hasPermanentMenuKey = false;
			}
		}
		return hasPermanentMenuKey;
	}
}
