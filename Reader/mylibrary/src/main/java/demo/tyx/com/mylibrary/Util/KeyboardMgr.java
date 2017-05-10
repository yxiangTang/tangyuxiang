package demo.tyx.com.mylibrary.Util;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 监听软键盘状态,只能在一种状态下监听,全屏与非全屏切换时有BUG
 */
public class KeyboardMgr
{
	protected View m_rootView;
	protected KeyboardMgr.Callback m_cb;

	protected int m_statusBarHeight;
	protected int m_currentStatusBarHeight;
	protected int m_keyboardHeight;
	protected boolean m_isShowKeyboard;

	public KeyboardMgr(Activity ac, View view, KeyboardMgr.Callback cb)
	{
		ShareData.InitData(ac);
		m_rootView = view.getRootView();
		m_cb = cb;

		if(ac != null)
		{
			m_statusBarHeight = ShareData.GetStatusBarHeight2(ac);
			m_currentStatusBarHeight = ShareData.GetCurrentStatusBarHeight(ac);
			if(m_rootView != null)
			{
				m_rootView.getViewTreeObserver().addOnGlobalLayoutListener(m_lst);
			}
		}
	}

	protected ViewTreeObserver.OnGlobalLayoutListener m_lst = new ViewTreeObserver.OnGlobalLayoutListener()
	{
		@Override
		public void onGlobalLayout()
		{
			if(m_rootView != null)
			{
				//应用可以显示的区域。此处包括应用占用的区域，不含状态栏和设备底部的虚拟按键。
				Rect r = new Rect();
				m_rootView.getWindowVisibleDisplayFrame(r);

				//屏幕高度，不含虚拟按键的高度
				int screenHeight = ShareData.m_screenHeight - m_currentStatusBarHeight;
				int heightDiff = screenHeight - (r.bottom - r.top);

				//修复全屏状态下在onCreate里调用的bug
				if(heightDiff != m_statusBarHeight)
				{
					//在不显示软键盘时，heightDiff等于状态栏的高度
					//在显示软键盘时，heightDiff会变大，等于软键盘加状态栏的高度。
					//所以heightDiff大于状态栏高度时表示软键盘出现了，
					//这时可算出软键盘的高度，即heightDiff减去状态栏的高度
					if(m_keyboardHeight == 0)
					{
						m_keyboardHeight = heightDiff; //初始化一次
					}

					if(m_isShowKeyboard)
					{
						//如果软键盘是弹出的状态，并且heightDiff小于等于状态栏高度，
						//说明这时软键盘已经收起
						if(heightDiff <= m_currentStatusBarHeight)
						{
							m_isShowKeyboard = false;
							if(m_cb != null)
							{
								m_cb.OnHideKeyboard(KeyboardMgr.this);
							}
						}
					}
					else
					{
						// 如果软键盘是收起的状态，并且heightDiff大于状态栏高度，
						// 说明这时软键盘已经弹出
						if(heightDiff > m_currentStatusBarHeight)
						{
							m_isShowKeyboard = true;
							if(m_cb != null)
							{
								m_cb.OnShowKeyboard(KeyboardMgr.this);
							}
						}
					}
				}
			}
		}
	};

	public int GetKeyboardHeight()
	{
		return m_keyboardHeight;
	}

	public void ClearAll()
	{
		if(m_rootView != null)
		{
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
			{
				m_rootView.getViewTreeObserver().removeGlobalOnLayoutListener(m_lst);
			}
			else
			{
				m_rootView.getViewTreeObserver().removeOnGlobalLayoutListener(m_lst);
			}
		}
		m_rootView = null;
		m_lst = null;
	}

	public static interface Callback
	{
		public void OnShowKeyboard(KeyboardMgr mgr);

		public void OnHideKeyboard(KeyboardMgr mgr);
	}
}
