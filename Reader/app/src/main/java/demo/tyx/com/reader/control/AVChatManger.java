//package demo.tyx.com.reader.control;
//
//import android.content.Context;
//import android.media.AudioManager;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log;
//
//import com.tencent.TIMConnListener;
//import com.tencent.TIMManager;
//import com.tencent.av.sdk.AVAudioCtrl;
//import com.tencent.av.sdk.AVAudioCtrl.Delegate;
//import com.tencent.av.sdk.AVCallback;
//import com.tencent.av.sdk.AVContext;
//import com.tencent.av.sdk.AVEndpoint;
//import com.tencent.av.sdk.AVError;
//import com.tencent.av.sdk.AVRoomMulti;
//import com.tencent.av.sdk.AVVideoCtrl;
//import com.tencent.av.sdk.AVVideoCtrl.EnableCameraCompleteCallback;
//import com.tencent.av.sdk.AVVideoCtrl.EnableExternalCaptureCompleteCallback;
//import com.tencent.av.sdk.AVView;
//import com.tencent.ilivesdk.ILiveCallBack;
//import com.tencent.ilivesdk.ILiveSDK;
//import com.tencent.ilivesdk.core.ILiveLoginManager;
//import com.tencent.ilivesdk.core.ILiveRoomManager;
//import com.tencent.ilivesdk.core.ILiveRoomOption;
//import com.tencent.openqq.IMSdkInt;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.channels.FileChannel;
//
//import static com.tencent.av.sdk.AVRoomMulti.AUTH_BITS_DEFAULT;
//
//public class AVChatManger {
//
//    Context mContext;
//    private String mSigStr;
//    long mRid = 0;//房间ID
//    private boolean ismic;//麦克风
//    private AVContext.StartParam mConfig;
//    OnLoginResultConnectCallBack mOnLoginResultConnectCallBack;//连接结果回调
//    OnRoomListener mOnRoomListener;
//    private AVContext mAvContext;
//    //房间类型，音视频sdk中0为AV_ROOM_NONE，1为AV_ROOM_PAIR，2为AV_ROOM_MULTI
//   /* public static final int AV_ROOM_NONE = AV_ROOM_NONE;
//    public static final int AV_ROOM_MULTI = AVRoom.AV_ROOM_MULTI;*/
////    int mRoomType = AV_ROOM_NONE;
//    //返回结果
//    public static final int AV_OK = 0;//成功
//    public static final int AV_ERROR = 1;//失败
//    public static final int AV_CHANGE = 2;//房间切换
//
//    private OnAudioOutputModeListener mOnOutputModeListener;//音频的输出设备监听器
//
//    private long mLastRid;//用来判断同类型房间切换
//
//    public static final int OUTPUT_MODE_HEADSET = AVAudioCtrl.OUTPUT_MODE_HEADSET;//耳机模式
//    public static final int OUTPUT_MODE_SPEAKER = AVAudioCtrl.OUTPUT_MODE_SPEAKER;//扬声器模式
//    private int mOutputMode = -1;
//    private boolean isOpenCamera = false;
//    static AVChatManger mAVChatManger;
//
//    private boolean isExitRoom = true;//为了判断是否换房间，还是真的退出房间
//
//    private boolean isVideo = true;
//
//    private boolean[] audioDataEnable = new boolean[AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_END];
//    private FileChannel[] audioDataNioChannel = new FileChannel[AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_END];
//    private ByteBuffer[] audioDataNioBuffer = new ByteBuffer[AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_END];
//    private int[] audioDataDataLen = new int[AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_END];
//    private FileInputStream[] audioDataFileInputStream = new FileInputStream[AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_END];
//
//    private Object obj = new Object();
//    private boolean isStopAvContext = false;
//    private MicChangeListenner micChangeListenner;
//
//    private boolean isInRoomFast = false;
//    private AVRoomMulti mAVRoomMulti;
//
//    private AVChatManger(Context context) {
//        super();
//        mContext = context;
//    }
//
//    public static AVChatManger getInstance(Context context) {
//        if (mAVChatManger == null) {
//            mAVChatManger = new AVChatManger(context);
//        }
//        return mAVChatManger;
//
//    }
//
//
//    public interface OnDrawFrameListener {
//        public void OnDrawFrame(int[] rgba, int w, int h);
//    }
//
//    public static void setAVChatManger(AVChatManger AVChatManger) {
//        mAVChatManger = AVChatManger;
//    }
//
//    //登录、sdk结果回调
//    public interface OnLoginResultConnectCallBack {
//        public void onLoginResult(int result);//登录账号
//    }
//    //房间监听器
//    public interface OnRoomListener {
//        // 创建房间成功回调
//        public void onEnterRoomComplete(int result);
//
//        // 离开房间成功回调
//        public void onExitRoomComplete();
//
//        //当房间内有成员状态发生变化时触发。
//        public void onEndpointsUpdateInfo(int eventid, String[] updateList);
//
//        //当房成员权限异常通知
//        public void OnPrivilegeDiffNotify(int privilege);
//
//        //半自动模式接收摄像头视频的事件通知
//        public void OnSemiAutoRecvCameraVideo(String[] identifierList);
//    }
//
//    //输出设备变化监听器，SDK自动切换耳机/扬声器/听筒
//    public interface OnAudioOutputModeListener {
//        public void onOutputModeChange(int outputMode);
//    }
//
//
//    /**
//     * 登录语音
//     */
//    public void loginAudioChat(String uid, int appId, int accType, String appId3rd, String sigStr) {
//        //预初始化
//        ILiveSDK.getInstance().initSdk(mContext, appId, accType);
//
//    login(uid,sigStr);
//
//
//      /*  //配置sdk系统
//        mConfig = new AVContext.StartParam();
//        mConfig.sdkAppId = appId;
//        mConfig.accountType = accType;
//        mConfig.appIdAt3rd = appId3rd;
//        mConfig.identifier = uid;//用户id
//        mSigStr = sigStr;
//
//
//        //请确保TIMManager.getInstance().init()一定执行在主线程
//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//            @Override
//            public void run() {
//                //设置连接环境 env - 1 - 测试环境   0 - 正式环境   ,不调用此接口，默认接入正式环境
////				TIMManager.getInstance().setEnv(1);
//
//
//                TIMManager.getInstance().init(mContext.getApplicationContext());
//                new Thread() {
//                    @Override
//                    public void run() {
//                        //登录
//                        login();
//                    }
//                }.start();
//            }
//        });*/
//    }
//
//
//    //登录
//    private void login(String uid,String sigStr) {
//
//        ILiveLoginManager.getInstance().iLiveLogin(uid, sigStr, new ILiveCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//
//                Log.d("cc", "登录成功init successfully. tiny id = " + IMSdkInt.get().getTinyId());
//                mOnLoginResultConnectCallBack.onLoginResult(AV_OK);
//            }
//
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//                Log.d("cc", "登录失败init failed, imsdk error code  = " + errCode + ", desc = " + errMsg);
//            }
//        });
//
//
//    }
//
//
//    /**
//     * 退出登录
//     */
//
//    private void logout() {
//        ILiveLoginManager.getInstance().iLiveLogout(new ILiveCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//
//            }
//
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//                Log.d("cc", "logout failed. code: " + errCode + " errmsg: " + errMsg);
//            }
//        });
//    }
//
//    /*
//     * 启动sdk
//     */
//    private void startSDK() {
//        mAvContext = getAvContext();
//
////			mAvContext.start( mStartContextCompleteCallback);
//        mAvContext.start(mConfig, mStartContextCompleteCallback);
//    }
//
//    //		//startContext()的回调函数，用来异步返回启动结果。
////		private AVContext.StartCallback mStartContextCompleteCallback = new AVContext.StartCallback() {
////
////
////			public void OnComplete(int result)
////			{
////				//异步返回启动结果之后的操作
////
////				 Log.d("cc","异步返回启动结果之后的操作="+result);
////				//创建房间+讨论组号
////				 if (result == AVError.AV_OK) {
////
////					//创建房间+讨论组号
//////					enterRoom(mRid,mRoomType);
////				}else {
////					mAvContext = null;
////				}
////
////				//sdk回调结果
////				 mOnLoginResultConnectCallBack.onLoginResult(result==AVError.AV_OK?AV_OK:AV_ERROR);;
////
////			}
////		};
////
//    //startContext()的回调函数，用来异步返回启动结果。
//    private AVCallback mStartContextCompleteCallback = new AVCallback() {
//
//
//        @Override
//        public void onComplete(int i, String s) {
//            //异步返回启动结果之后的操作
//
//            Log.d("cc", "异步返回启动结果之后的操作=" + s);
//            //创建房间+讨论组号
//            if (i == AVError.AV_OK) {
//
//                //创建房间+讨论组号
////					enterRoom(mRid,mRoomType);
//            } else {
//                mAvContext = null;
//            }
//
//            //sdk回调结果
//            mOnLoginResultConnectCallBack.onLoginResult(i == AVError.AV_OK ? AV_OK : AV_ERROR);
//        }
//
//
//    };
//
////
////    public void createAvContext() {
////        if (mAvContext == null)
////            mAvContext = AVContext.createInstance(mContext);
////    }
//
//
//    /**
//     * 创建房间.注：需要在主线程中用
//     *
//     * @param relationId 讨论组号
//     */
//    public void enterRoom(int relationId) {
//
//
//
//        //创建房间配置项
//        ILiveRoomOption hostOption = new ILiveRoomOption(null).
//                controlRole("Host")//角色设置
//                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)//权限设置
////                .cameraId(ILiveConstants.FRONT_CAMERA)//摄像头前置后置
//                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);//是否开始半自动接收
//        //创建房间
//        ILiveRoomManager.getInstance().createRoom(relationId, hostOption, new ILiveCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//                if (mOnRoomListener != null) {
//                    mOnRoomListener.onEnterRoomComplete(AV_OK);
//                }
//            }
//
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//                if (mOnRoomListener != null) {
//                    mOnRoomListener.onEnterRoomComplete(AV_ERROR);
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 离开房间
//     */
//    public void quitRoom(){
//        ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
//            @Override
//            public void onSuccess(Object data) {
//                if (mOnRoomListener != null) {
//                    //房间结果回调
//
//                    mOnRoomListener.onExitRoomComplete();
//                }
//            }
//
//            @Override
//            public void onError(String module, int errCode, String errMsg) {
//
//            }
//        });
//
//    }
//
//
//
//    //多人房间委托类
//    private AVRoomMulti.EventListener mMultiDelegate = new AVRoomMulti.EventListener() {
//
//
//        @Override
//        public void onEnterRoomComplete(int i) {
//            isInRoomFast = true;
//
//            Log.d("cc", "多人房间房间创建onEnterRoomComplete = " + i);
//            int mStaus;
//            if (i == AVError.AV_OK) {
//                //开始音频
//                startAudio();
//                /*if (isVideo) {
//                    startVideo();
//				}*/
//                mStaus = AV_OK;
//            } else {
////                mRoomType = AV_ROOM_NONE;//进入房间失败，设置为none房间
//                mStaus = AV_ERROR;
//            }
//            //房间结果回调
//            if (mOnRoomListener != null) {
//                mOnRoomListener.onEnterRoomComplete(mStaus);
//            }
//        }
//
//        @Override
//        public void onExitRoomComplete() {
//            Log.d("cc", "多人房间离开房间WL_DEBUG mRoomDelegate.onExitRoomComplete");
//           /* int mStaus;
//            if (i == AVError.AV_OK) {
//                mStaus = AV_OK;
//            } else {
//                mStaus = AV_ERROR;
//            }*/
//
//					/*if (isExitRoom == false) {
//                        Log.d("cc", "退出多人房间，进入其他房间onExitRoomComplete 房间ID= " + mRid);
//						enterRoom(mRid,mRoomType);
//						mStaus = AV_CHANGE;//就是非AV_OK,目的是为了不让他变成封面图，保持聊天室界面
//					}*/
//
//            if (mOnRoomListener != null) {
//                //房间结果回调
//                mOnRoomListener.onExitRoomComplete();
//            }
//
//
////            if (isStopAvContext) {
////                mAvContext.stop(new AVContext.StopCallback() {
////                    @Override
////                    public void OnComplete() {
////                        isStopAvContext = false;
////                        mAvContext.destroy();
////                        mAvContext = null;
////                    }
////                });
////            }
//        }
//
//        @Override
//        public void onRoomDisconnect(int i) {
//
//        }
//
//        @Override
//        public void onEndpointsUpdateInfo(int i, String[] strings) {
//            //    endpoint_count - 发生状态更新的人数。
//            // endpoint_list - 发生状态更新的房间成员列表。
//            Log.d("cc", "多人房间当房间内有成员状态发生变化时触发。WL_DEBUG onEndpointsUpdateInfo. eventid = " + strings);
//            if (mOnRoomListener != null) {
//                mOnRoomListener.onEndpointsUpdateInfo(i, strings);
//            }
//        }
//
//        @Override
//        public void onPrivilegeDiffNotify(int i) {
//
//        }
//
//        @Override
//        public void onSemiAutoRecvCameraVideo(String[] strings) {
//
//        }
//
//        @Override
//        public void onCameraSettingNotify(int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onRoomEvent(int i, int i1, Object o) {
//
//        }
//    };
//
//
//    //通知输出设备变化,SDK自动切换耳机/扬声器/听筒
//    private Delegate mDelegate = new Delegate() {
//        @Override
//        protected void onOutputModeChange(int outputMode) {
//            super.onOutputModeChange(outputMode);
//            Log.d("cc", "通知输出设备变化Delegate:outputMode=" + outputMode);
//            mOutputMode = outputMode;
//            mOnOutputModeListener.onOutputModeChange(outputMode);
//
//        }
//    };
//
//    //开始音频
//    private void startAudio() {
//        final AVAudioCtrl avAudioCtrl = mAvContext.getAudioCtrl();
//        avAudioCtrl.setDelegate(mDelegate);
//
//        boolean isSpeaker = avAudioCtrl.enableSpeaker(true);
//        if (mOutputMode != -1) {
//            avAudioCtrl.setAudioOutputMode(mOutputMode);
//        } else {
//            if (mAvContext != null && mAvContext.getAudioCtrl() != null) {
//                if (getOutputMode() == OUTPUT_MODE_SPEAKER) {
//                    //从Android SDK打开扬声器
//                    AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//                    mAudioManager.setSpeakerphoneOn(true);
//
//                    avAudioCtrl.setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_SPEAKER);
//                } else if (getOutputMode() == OUTPUT_MODE_HEADSET) {
//                    avAudioCtrl.setAudioOutputMode(AVAudioCtrl.OUTPUT_MODE_HEADSET);
//                }
//            } else {
//            }
//
//        }
//    }
//
//
//    public void setNetType(int netType) {
//
//        AVRoomMulti room = (AVRoomMulti) mAvContext.getRoom();
//        if (null != room) {
//            room.setNetType(netType);
//        }
//    }
//
//
//    public int setEnable(int src_type, boolean enable) {
//        audioDataEnable[src_type] = enable;
//        if (src_type == AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_MIXTOSEND || src_type == AVAudioCtrl.AudioDataSourceType.AUDIO_DATA_SOURCE_MIXTOPLAY) {
//            if (!enable) {
//                synchronized (obj) {
//                    if (audioDataNioChannel[src_type] != null) {
//                        try {
//                            audioDataNioChannel[src_type].close();
//                            audioDataFileInputStream[src_type].close();
//
//                        } catch (IOException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        } finally {
//                            audioDataNioBuffer[src_type] = null;
//                            audioDataNioChannel[src_type] = null;
//                            audioDataDataLen[src_type] = 0;
//                            audioDataFileInputStream[src_type] = null;
//                        }
//                    }
//                }
//            }
//        }
//        Log.e("auido", "audio setEnable src_type = " + src_type + ", enable = " + enable);
//        return 0;
//    }
//
//
//    public void startVideo() {
////			 mAvContext.setRenderFunctionPtr(mGraphicRenderMgr.getRecvDecoderFrameFunctionptr());
//        if (mAvContext != null && mAvContext.getRoom() != null) {
//            AVVideoCtrl avVideoCtrl = mAvContext.getVideoCtrl();
//
//            int enableExternalCapture = avVideoCtrl.enableExternalCapture(true, mEnableExternalCaptureCompleteCallback);
//
//        }
////			 boolean preCall = avVideoCtrl.setRemoteVideoPreviewCallback(mRemoteVideoPreviewCallback);
////			 Log.d("dd", "preCall=="+preCall);
//    }
//
//
//    public void stopVideo() {
////			 mAvContext.setRenderFunctionPtr(mGraphicRenderMgr.getRecvDecoderFrameFunctionptr());
//
//        if (mAvContext != null && mAvContext.getRoom() != null) {
//
//            AVVideoCtrl avVideoCtrl = mAvContext.getVideoCtrl();
//
//            int enableExternalCapture = avVideoCtrl.enableExternalCapture(false, mEnableExternalCaptureCompleteCallback);
//
//
//            Log.d("dd", "enableExternalCapture==" + enableExternalCapture);
//
////			 boolean preCall = avVideoCtrl.setRemoteVideoPreviewCallback(mRemoteVideoPreviewCallback);
////			 Log.d("dd", "preCall=="+preCall);
//        }
//    }
//
//	/*public void startCameraVideo() {
//        AVVideoCtrl avVideoCtrl = mAvContext.getVideoCtrl();
//
//		int cameraNum = avVideoCtrl.getCameraNum();
//		Log.d("dd", "cameraNum==" + cameraNum);
//
//		Object i = avVideoCtrl.getCamera();
//
//		int enableCamera = avVideoCtrl.enableCamera(0, true, mEnableCameraCompleteCallback);
//		Log.d("dd", "enableCamera==" + enableCamera);
//
////			 boolean preCall = avVideoCtrl.setRemoteVideoPreviewCallback(mRemoteVideoPreviewCallback);
////			 Log.d("dd", "preCall=="+preCall);
//	}*/
//
//    //外部视频回调
//    private EnableExternalCaptureCompleteCallback mEnableExternalCaptureCompleteCallback = new EnableExternalCaptureCompleteCallback() {
//        @Override
//        protected void onComplete(boolean enable, int result) {
//            super.onComplete(enable, result);
//
//            if (result == AVError.AV_OK) {
//                if (mExternalCaptureCompleteCallback != null) {
//                    mExternalCaptureCompleteCallback.onComplete(enable, result);
//                }
//                //isOpenCamera=enable;
//            }
//
//        }
//    };
//
//
//    //打开腾讯sdk相机回调
//    private EnableCameraCompleteCallback mEnableCameraCompleteCallback = new EnableCameraCompleteCallback() {
//        protected void onComplete(boolean enable, int result) {
//            super.onComplete(enable, result);
//
//            if (result == AVError.AV_OK) {
//
//                isOpenCamera = true;
//            } else {
//                isOpenCamera = false;
//            }
//
//        }
//    };
//
//    private ExternalCaptureCompleteCallback mExternalCaptureCompleteCallback;
//
//    public interface ExternalCaptureCompleteCallback {
//        public void onComplete(boolean enable, int result);
//    }
//
//
//    public ExternalCaptureCompleteCallback getmExternalCaptureCompleteCallback() {
//        return mExternalCaptureCompleteCallback;
//    }
//
//    public void setmExternalCaptureCompleteCallback(
//            ExternalCaptureCompleteCallback mExternalCaptureCompleteCallback) {
//        this.mExternalCaptureCompleteCallback = mExternalCaptureCompleteCallback;
//    }
//
///*    //远程视频流数据回调
//    public RemoteVideoPreviewCallback mRemoteVideoPreviewCallback = new RemoteVideoPreviewCallback() {
//        public void onFrameReceive(VideoFrame videoFrame) {
//
//            Log.d("dd", "real RemoteVideoPreviewCallback.onFrameReceive");
//            Log.d("dd", "len: " + videoFrame.dataLen);
//            Log.d("dd", "identifier: " + videoFrame.identifier);
//            Log.d("dd", "videoFormat: " + videoFrame.videoFormat);
//            Log.d("dd", "width: " + videoFrame.width);
//            Log.d("dd", "height: " + videoFrame.height);
//
//        }
//
//        ;
//    };*/
//
//    /**
//     * @param data        图像数据。
//     * @param dataLen     图像数据长度。
//     * @param width       图像宽度。
//     * @param height      图像高度。
//     * @param cameraAngle 图像渲染角度。角度可以是0，90，180，270。
//     *                    //		 * @param colorFormat 图像颜色格式。当前仅支持COLOR_FORMAT_I420。
//     *                    //		 * @param srcType 视频源类型。当前仅支持VIDEO_SRC_TYPE_CAMERA。
//     *                    //		 * @return
//     */
//    public boolean videowriteframe(byte[] data, int dataLen, int width, int height, int cameraAngle) {
//        if (mAvContext == null) {
//            return false;
//        }
//
//
//        //分辨率宽高比例限定只能为4：3，且最大宽度为640
//
//        try {
//
//            if (mAvContext != null) {
//                AVVideoCtrl avVideoCtrl = mAvContext.getVideoCtrl();
//
//                if (data != null && avVideoCtrl != null) {
//                    int fillExternalCaptureFrame = avVideoCtrl.fillExternalCaptureFrame(data, dataLen, width, height, cameraAngle, AVVideoCtrl.COLOR_FORMAT_I420, AVView.VIDEO_SRC_TYPE_CAMERA);
//                    if (fillExternalCaptureFrame == AVError.AV_OK) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//
//        }
//        return false;
//
//    }
//
//    private void onClose() {//全部关闭
//        if (mAvContext != null) {//关闭音频context，先关闭AVContext对象。再销毁AVContext。
//            if (mAvContext.getAudioCtrl() != null) {
////					mAvContext.getAudioCtrl().uninit();
//                mAvContext.getAudioCtrl().stopTRAEService();
//            }
//            exitRoom();
//            logout();
//            isStopAvContext = true;
//            mAVChatManger = null;
//
//            isInRoomFast = false;
//
//        }
//    }
//
////		public void onResume() {
////			if (mAvContext != null) {//恢复音频context
////				mAvContext.onResume();
//////				enterRoom(mRid,mRoomType);
////			}
////		}
////
////		public void onPause() {
////			if (mAvContext != null) {//暂停音频
////				mAvContext.onPause();
////			}
////		}
//
//
//    public AVContext getAvContext() {
//        if (mAvContext == null) {
////				mAvContext = AVContext.createInstance(mContext,mConfig);
//
//        }
//        return mAvContext;
//    }
//
//
//    public Boolean hasAVContext() {
//        if (mAvContext != null) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//    public Boolean isOpenCamera() {
//        return isOpenCamera;
//    }
//
////    public Config getAvConfig() {
////        return mConfig;
////    }
//
//    public String getSig() {
//        return mSigStr;
//    }
//
//    /**
//     * 登录连接结果回调
//     *
//     * @return
//     */
//    public void setOnLoginResultConnectCallBack(OnLoginResultConnectCallBack onLoginResultConnectCallBack) {
//        mAVChatManger.mOnLoginResultConnectCallBack = onLoginResultConnectCallBack;
//    }
//
//    /**
//     * 得到房间的类型，如多人，双人，没人
//     *
//     * @return
//     */
////    public int getRoomType() {
////        return mRoomType;
//////    }
//
//    /**
//     * 设置房间的类型，如多人，双人，没人
//     *
//     * @return
//     */
// /*   public void setRoomType(int mRoomType) {
//        mAVChatManger.mRoomType = mRoomType;
////    }*/
//
//    /**
//     * 房间
//     *
//     * @return
//     */
//    public AVRoomMulti getRoom() {
//        return getAvContext().getRoom();
//    }
///**
// *
// */
//
//    /**
//     * 房间ID
//     *
//     * @return
//     */
//    public Long getRoomID() {
//        return mRid;
//    }
//
//    /**
//     * 得到指定的多人房间成员
//     *
//     * @return
//     */
//    public AVEndpoint getMultiEndpoint(String id) {
//        return getRoom().getEndpointById(id);
//    }
//
//    /**
//     * 得到指定的房间成员
//     *
//     * @return
//     */
///*    public AVEndpoint getEndpoint(String id) {
//        AVEndpoint avEndpoint = null;
//        if (mRoomType == AV_ROOM_MULTI) {
//            avEndpoint = getMultiEndpoint(id);
//        }
////			else if (mRoomType == AV_ROOM_PAIR) {//1.4语音版本
////				avEndpoint =  getPairEndpoint(id);
////			}
//        return avEndpoint;
//    }*/
//
//    /**
//     * 设置连接结果回调
//     *
//     * @return
//     */
//    public void setOnResultConnectCallBack(
//            OnLoginResultConnectCallBack mOnResultConnectCallBack) {
//        mAVChatManger.mOnLoginResultConnectCallBack = mOnResultConnectCallBack;
//    }
//
//    /**
//     * 设置房间状态监听器
//     *
//     * @return
//     */
//    public void setOnRoomListener(OnRoomListener mOnRoomListener) {
//        mAVChatManger.mOnRoomListener = mOnRoomListener;
//    }
//
//    public void setOnAudioOutputModeListener(OnAudioOutputModeListener onOutputModeListener) {
//        this.mOnOutputModeListener = onOutputModeListener;
//    }
//
//
//    public boolean isVideo() {
//        return isVideo;
//    }
//
//    public void setVideo(boolean isVideo) {
//        this.isVideo = isVideo;
//    }
//
//    /**
//     * 设置输出设备模式
//     *
//     * @param outputMode
//     */
//    public void setOutputMode(int outputMode) {
//        mAvContext.getAudioCtrl().setAudioOutputMode(outputMode);
//    }
//
//    /**
//     * 得到输出设备模式
//     */
//    public int getOutputMode() {
//        if (mAvContext != null && mAvContext.getAudioCtrl() != null) {
//            return mAvContext.getAudioCtrl().getAudioOutputMode();
//        }
//        return mOutputMode;
//    }
//
//
//    /**
//     * 设置麦克风监听
//     */
//
//    public interface MicChangeListenner {
//        void micChange(Boolean isMic);
//    }
//
//    public void setMicChangeListenner(MicChangeListenner micChangeListenner) {
//        this.micChangeListenner = micChangeListenner;
//    }
//
//
//    //	boolean isMic = true;
//    Handler handler = new Handler(Looper.getMainLooper());
////	Runnable runnable = new Runnable() {
////		public void run() {
////
////			if (mAvContext != null && mAvContext.getAudioCtrl() != null && isRoom()) {
////				boolean enableMic = mAvContext.getAudioCtrl().enableMic(isMic);
////				Log.d("bb", " enableMic==" + enableMic + " 	---getDynamicVolume()==" + mAvContext.getAudioCtrl().getDynamicVolume() + " 	麦克风---getVolume()==" + mAvContext.getAudioCtrl().getVolume());
//////					if (enableMic == false) {
////
////				handler.removeCallbacks(runnable);
////				handler.postDelayed(runnable, 500);
//////					}
////			} else {
////
////			}
////		}
////	};
//
////	public void setMic(boolean isMic) {
////		this.isMic = isMic;
////		handler.post(runnable);
////
////	}
//
//
//    public void setMic(final boolean isMic) {
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mAvContext != null && mAvContext.getAudioCtrl() != null && isRoom()) {
//                    boolean enableMic = mAvContext.getAudioCtrl().enableMic(isMic);
//                    if (enableMic) {
//                        if (micChangeListenner != null) {
//                            micChangeListenner.micChange(true);
//                        }
//                    }
//                    Log.d("bb", " enableMic==" + enableMic + " 	---getDynamicVolume()==" + mAvContext.getAudioCtrl().getDynamicVolume() + " 	麦克风---getVolume()==" + mAvContext.getAudioCtrl().getVolume());
////					if (enableMic == false) {
//
////					handler.removeCallbacks(runnable);
////				handler.postDelayed(runnable, 500);
////					}
//                } else {
//
//                }
//            }
//        });
//
//
//    }
//
//
//    public void stopMic(final boolean isMic) {
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (mAvContext != null && mAvContext.getAudioCtrl() != null && isRoom()) {
//                    boolean enableMic = mAvContext.getAudioCtrl().enableMic(isMic);
//                    if (enableMic) {
//                        if (micChangeListenner != null) {
//                            micChangeListenner.micChange(false);
//                        }
//                    }
//                    Log.d("bb", " enableMic==" + enableMic + " 	---getDynamicVolume()==" + mAvContext.getAudioCtrl().getDynamicVolume() + " 	麦克风---getVolume()==" + mAvContext.getAudioCtrl().getVolume());
////					if (enableMic == false) {
//
//
////					}
//                } else {
//
//                }
//            }
//        });
//
//
//    }
////		/**
////		 * 停止语音的功能，但语音房间依然在
////		 */
////		public void stopAudioFunction() {
////			if (mAvContext!=null&&mAvContext.getAudioCtrl()!=null) {
////				mAvContext.getAudioCtrl().stopTRAEService();
////				mAvContext.getAudioCtrl().stopTRAEServiceWhenIsSystemApp();
////			}
////
////		}
////		/**
////		 * 开始语音的功能，但只是功能
////		 */
////		public void startAudioFunction() {
////			if (mAvContext!=null&&mAvContext.getAudioCtrl()!=null) {
////				mAvContext.getAudioCtrl().startTRAEService();
////				mAvContext.getAudioCtrl().startTRAEServiceWhenIsSystemApp();
////			}
////
////		}
//
//    /**
//     * 是否存在聊天房间
//     */
//    public boolean isRoom() {
//        if (mAvContext != null && mAvContext.getRoom() != null) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//  /*  *//**
//     * 不建议用，是否存在聊天房间
//     *//*
//    public boolean isRoomFast() {
//        return isInRoomFast;
//    }*/
//
//    /**
//     * 登录腾讯云连接的监听器
//     */
//    public void setLoginConnectListener(TIMConnListener mTimConnListener) {
//        TIMManager.getInstance().setConnectionListener(mTimConnListener);
//    }
//
//
//    /**
//     * 退出房间
//     */
//    public void exitRoom() {
//        if (isRoom()) {
////				if (mAvContext.getAudioCtrl() != null) {
////					mAvContext.getAudioCtrl().stopTRAEService();
////				}
//            isExitRoom = true;
//            mAvContext.exitRoom();
//
//        }
//        if (isStopAvContext) {
//            mAvContext.stop();
//            mAvContext.destroy();
//            mAvContext=null;
//        }
//    /*    new AVContext.StopCallback() {
//            @Override
//            public void OnComplete() {
//                isStopAvContext = false;
//                mAvContext.destroy();
//                mAvContext = null;
//            }
//        }*/
//    }
//
//    /**
//     * 退出，包括登录、sdk等，全部都退出
//     */
//    public void exit() {
//        onClose();
//    }
//
//
////	public boolean setLocalHasVideo(boolean isLocalHasVideo, boolean forceToBigView, String identifier) {
////		if (mContext == null)
////			return false;
////
////		if (Utils.getGLVersion(mContext) == 1) {
////			return false;
////		}
////
////
////		if (isLocalHasVideo) {// 打开摄像头
////			GLVideoView view = null;
////			int index = getViewIndexById(identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
////			if (index < 0) {
////				index = getIdleViewIndex(0);
////				if (index >= 0) {
////					view = mGlVideoView[index];
////					view.setRender(identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
////					localViewIndex = index;
////				}
////			} else {
////				view = mGlVideoView[index];
////			}
////			if (view != null) {
////				view.setIsPC(false);
////				view.enableLoading(false);
////				// if (isFrontCamera()) {
////				// view.setMirror(true);
////				// } else {
////				// view.setMirror(false);
////				// }
////				view.setVisibility(GLView.VISIBLE);
////			}
////			if (forceToBigView && index > 0) {
////				switchVideo(0, index);
////			}
////		} else if (!isLocalHasVideo) {// 关闭摄像头
////			int index = getViewIndexById(identifier, AVView.VIDEO_SRC_TYPE_CAMERA);
////			if (index >= 0) {
////				closeVideoView(index);
////				localViewIndex = -1;
////			}
////		}
////		mIsLocalHasVideo = isLocalHasVideo;
////
////		return true;
////	}
////
////
////
////	int getViewIndexById(String identifier, int videoSrcType) {
////		int index = -1;
////		if (null == identifier) {
////			return index;
////		}
////		for (int i = 0; i < mGlVideoView.length; i++) {
////			GLVideoView view = mGlVideoView[i];
////			if ((identifier.equals(view.getIdentifier()) && view.getVideoSrcType() == videoSrcType) && view.getVisibility() == GLView.VISIBLE) {
////				index = i;
////				break;
////			}
////		}
////		return index;
////	}
//}