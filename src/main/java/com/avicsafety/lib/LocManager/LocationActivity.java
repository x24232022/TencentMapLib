package com.avicsafety.lib.LocManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;


public class LocationActivity extends Activity implements TencentLocationListener {
	
	private Activity oThis;
	private MapView mapView;
	private TencentMap tencentMap;
	
	private TencentLocationManager locationManager;
	private TencentLocationRequest locationRequest;
	
	private double lat;  //纬度
	private double lng;   //经度
	
	private TextView tv_back;
	private TextView tv_title;
	private Button btn_gps;
	
	private Button btn_NORMAL;
	private Button btn_SATELLITE;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_com_avicsafety_lib_company_location);
	    oThis = this;

        initUI();
        initData();
        initEvent();
	}

	private void initUI() {
		// TODO Auto-generated method stub
		tv_title =(TextView) this.findViewById(R.id.tv_title);
        tv_back = (TextView) this.findViewById(R.id.tv_back);
        btn_gps = (Button) this.findViewById(R.id.btn_gps);
        mapView = (MapView) this.findViewById(R.id.map);
        btn_NORMAL = (Button) this.findViewById(R.id.btn_NORMAL);
        btn_SATELLITE = (Button) this.findViewById(R.id.btn_SATELLITE);
        tv_title.setText("拖动地图选择位置");
	}
	
	private void initData() {
		// TODO Auto-generated method stub
		locationManager = TencentLocationManager.getInstance(this);
		locationRequest = TencentLocationRequest.create();
		
		Intent intent = getIntent();
		lat = intent.getDoubleExtra("lat", 0);
		lng = intent.getDoubleExtra("lng", 0);
		
		if(lat==0||lng==0){
			lat =41.792274;
			lng = 123.432556;
			
			
			 GPS_INIT();
		}else{
			Log.v("tag", "have postion info lat is "+ lat +", lng is"+lng);
		}
		

        tencentMap = mapView.getMap();
//        tencentMap.setZoom(16);
        //tencentMap.setCenter());
//        tencentMap.animateTo(new LatLng(lat,lng));
        
        
        CameraUpdate cameraSigma =
        		CameraUpdateFactory.newCameraPosition(new CameraPosition(
        	            new LatLng(lat,lng), //新的中心点坐标
        	            16,  //新的缩放级别
        	            0f, //俯仰角 0~45° (垂直地图时为0)
        	            0f)); //偏航角 0~360° (正北方为0)
        tencentMap.moveCamera(cameraSigma);
	}
	
	private void initEvent() {
		// TODO Auto-generated method stub
//		 tencentMap.setOnMapCameraChangeListener(new OnMapCameraChangeListener(){
//				@Override
//				public void onCameraChange(CameraPosition arg0) {
//
//				}
//
//				@Override
//				public void onCameraChangeFinish(CameraPosition arg0) {
//					// TODO Auto-generated method stub
//					Log.v("tag", "onCameraChangeFinish...  lat is "+arg0.getTarget().getLatitude() +"  long is "+ arg0.getTarget().getLongitude());
//					lat = arg0.getTarget().getLatitude();
//					lng = arg0.getTarget().getLongitude();
//				}});
		 TencentMap.OnCameraChangeListener listener = new TencentMap.OnCameraChangeListener() {
			 
		        // 视图变化完成
		        @Override
		        public void onCameraChangeFinished(CameraPosition arg0) {
		            // TODO Auto-generated method stub
		        	Log.v("tag", "onCameraChangeFinish...  lat is "+arg0.target.latitude +"  long is "+ arg0.target.longitude);
					lat = arg0.target.latitude;
					lng = arg0.target.longitude;
		        }
		         
		        // 视图变化中
		        @Override
		        public void onCameraChange(CameraPosition arg0) {
		            // TODO Auto-generated method stub
		             
		        }
		};
		 
		// 绑定视图变化监听事件
		tencentMap.setOnCameraChangeListener(listener);
		
		
		 tv_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}});
		 
		 btn_gps.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.putExtra("lat", lat);
					intent.putExtra("lng", lng);
					setResult(RESULT_OK, intent);
					finish();
				}});
		 btn_NORMAL.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					tencentMap.setSatelliteEnabled(false);
					
					tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
//					 tencentMap = mapView.getMap();
//					tencentMap.animateTo(new LatLng(lat,lng));
				}});
		 btn_SATELLITE.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					tencentMap.setSatelliteEnabled(true);
					tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
//					tencentMap.setTrafficEnabled(true);
//					 tencentMap = mapView.getMap();
//					tencentMap.animateTo(new LatLng(lat,lng));
				}});
	}


	private void GPS_INIT() {

			int error = locationManager.requestLocationUpdates(locationRequest,this);
			switch (error) {
			case 0:
				Log.e("location", "成功注册监听器");
				break;
			case 1:
				Log.e("location", "设备缺少使用腾讯定位服务需要的基本条件");
				break;
			case 2:
				Log.e("location", "manifest 中配置的 key 不正确");
				break;
			case 3:
				Log.e("location", "自动加载libtencentloc.so失败");
				break;

			default:
				break;
			}
//			sensorManager.registerListener(ShowMyLocationActivity.this, 
//					oritationSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
                finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
	
	 @Override
	    protected void onDestroy() {
	        super.onDestroy();
	    }
	 
	    @Override
	    protected void onPause() {
	    	mapView.onPause();
	        super.onPause();
	    }
	 
	    @Override
	    protected void onResume() {
	    	mapView.onResume();
	        super.onResume();
	    }
	 
	    @Override
	    protected void onStop() {
	    	mapView.onStop();
	        super.onStop();
	    }



		@Override
		public void onLocationChanged(TencentLocation arg0, int arg1,
				String arg2) {
			// TODO Auto-generated method stub
			if (arg1 == TencentLocation.ERROR_OK) {
				LatLng latLng = new LatLng(arg0.getLatitude(), arg0.getLongitude());
//				if (myLocation == null) {
//					myLocation = tencentMap.addMarker(new MarkerOptions().
//							position(latLng).
//							//icon(BitmapDescriptorFactory.fromResource(R.drawable.navigation)).
//							anchor(0.5f, 0.5f));
//				}
//				if (accuracy == null) {
//					accuracy = tencentMap.addCircle(new CircleOptions().
//							center(latLng).
//							radius((double)arg0.getAccuracy()).
//							fillColor(0x440000ff).
//							strokeWidth(0f));
//				}
//				myLocation.setPosition(latLng);
//				myLocation.setRotation(arg0.getBearing()); //仅当定位来源于gps有效，或者使用方向传感器
//				accuracy.setCenter(latLng);
//				accuracy.setRadius(arg0.getAccuracy());
				//tencentMap.setCenter(latLng)
//				tencentMap.getCameraPosition();
				 CameraUpdate cameraSigma =
			        		CameraUpdateFactory.newLatLng(latLng);
				tencentMap.animateCamera(cameraSigma);
//				tencentMap.animateCamera();
				//取消GPS定位
				locationManager.removeUpdates(this);

			}
			
			//tencentMap.setCenter(new LatLng(arg0.getLatitude(),arg0.getLongitude()));
		}

		@Override
		public void onStatusUpdate(String arg0, int arg1, String arg2) {
			// TODO Auto-generated method stub
			
		}
	
	
	
	//{@ViewInject} /////////////////////////////////////////////////////////////////////////////
	
	

	
}
