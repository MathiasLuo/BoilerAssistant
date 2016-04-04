package me.mathiasluo;

import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.InjectView;
import me.mathiasluo.R;
import me.mathiasluo.activity.MainActivity;
import me.mathiasluo.base.BaseFragment;

/**
 * Created by mathiasluo on 16-3-26.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
  /*  @InjectView(R.id.webView)
    ProgressWeb mWebView;*/
  private GoogleMap mMap;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_map;
    }

    @Override
    public void onResume() {
        super.onResume();
        mToolbar.setTitle("Map");
        ((MainActivity) getActivity()).setToolbar(mToolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(40.433334,-86.900002);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
