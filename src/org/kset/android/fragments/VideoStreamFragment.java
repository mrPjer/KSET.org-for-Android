package org.kset.android.fragments;

import org.kset.android.R;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

/**
 * Fragment which contains the video stream from
 * http://zaphod.kset.org:8000/audiovideo.ogg
 * 
 * @author Petar Šegina <psegina@kset.org>
 * 
 */
public class VideoStreamFragment extends Fragment implements OnClickListener{

	private VideoView w;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.stream_fragment, container, false);		
		v.findViewById(R.id.stream_play).setOnClickListener(this);
		w = (VideoView) v.findViewById(R.id.stream_video);
//		w.setVideoURI(Uri.parse("http://zaphod.kset.org:8000/audiovideo.ogg"));
		w.setVideoURI(Uri.parse("http://o-o.preferred.cix-zag1.v4.lscache2.c.youtube.com/videoplayback?sparams=id%2Cexpire%2Cip%2Cipbits%2Citag%2Csource%2Cratebypass%2Ccp&fexp=906011&itag=18&ip=93.0.0.0&signature=99DD81E5B6144629650E2F15E0047958A485DC67.86A7A9001674A91EC4B0597E1C5914A7BBE858DB&sver=3&ratebypass=yes&source=youtube&expire=1326067647&key=yt1&ipbits=8&cp=U0hRS1BRVV9JUUNOMV9IS1lHOk9DNXplOUJ2SFhI&id=270d51e34951948e&title=Hladno%20Pivo%20-%20Samo%20Za%20Taj%20Osje%C4%87aj%20spot%20HQ"));
		return v;
	}
	
	@Override
	public void onClick(View v) {
		if(w.isPlaying()){
			w.stopPlayback();
			((Button) v).setText("Start stream");
		} else{
			w.start();
			((Button) v).setText("Stop stream");
		}
	}
	
}
