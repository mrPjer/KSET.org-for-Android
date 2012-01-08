package org.kset.android.fragments;

import org.kset.android.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

/**
 * A simple Fragment which displays information about the organisation
 * 
 * @author Petar Šegina <psegina@kset.org>
 * 
 */
@SuppressWarnings("deprecation")
public class AboutKsetFragment extends Fragment {

	private WebView webview;
	private static final String WEB_LOCATION = "http://www.kset.org";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		webview = new WebView(getActivity());
		webview.loadDataWithBaseURL(
				"http://www.kset.org",
				"<h1>KSET - Klub Studenata ElektroTehnike</h1><p>To je biv&scaron;a <strong>kotlovnica</strong> smje&scaron;tena na sjeveroistočnom uglu najboljeg fakulteta na svijetu (i pored C zgrade FER-a). Neki ljudi tvrde da je \"<strong>Rupa</strong>\" unatoč brojnim nelogičnostima te izjave. Za početak, Rupa je mjesto u sjeverozapadnoj Hrvatskoj na granici sa Slovenijom.</p><p>U dana&scaron;njem obliku <strong>Klub postoji već 34 godine</strong>. On funkcionira vrlo dobro, hvala na pitanju, i broji <strong>9 sekcija</strong>: <strong>bike </strong>(profesionalni obožavatelji međunožnog gurala), <strong>disco</strong> (navodno za muziku), <strong>dramska</strong> (misle da znaju glumiti), <strong>računarska</strong> (za geekove i geekice), <strong>tehnička</strong> (za manualni rad), <strong>glazbena</strong> (za propale glazbenike koji su se bacili u opremanje, ma imaju oni i bend), <strong>planinarska</strong> a.k.a. P.I.J.A.N.D.U.R.E. (s redovitim gubljenjem po &scaron;umama upoznaju prirodu i svoj unutra&scaron;nji mir), <strong>video</strong> (snimaju i opskrbljuju KSET onim prekrasnim bićima koje nazivamo žene) i <strong>foto</strong> (za umjetnike s pravim aparatima). Vi&scaron;e o njima možete saznati kasnije ili navratite do KSET-a i pitajte prvu osobu na koju naiđete.</p>	<p><img src=\"file:///android_asset/kset-dvorana.jpg\" alt=\"\" /></p><p>Sada koja riječ o nekim administrativni stvarima. <strong>KSET je klub volontera</strong>. To znači da sve &scaron;to radimo je rezultat činjenice da nemamo pametnijeg posla u životu. Za&scaron;to bi to itko radio? <strong>Jer je zabavno</strong>. KSET postane mjesto na kojem možete <strong>popiti kavu</strong>, sjesti s <strong>prijateljima</strong> i <strong>kartati</strong> ili <strong>surfati</strong> u pauzama između <strong>predavanja</strong> ili se na kraju dana opustiti uz <strong>&scaron;ah</strong> ili dobar <strong>film</strong>.</p><p>Za vrijeme ispita Klub se pretvara u mjesto u kojem svi uče, ali će članovi uvijek izdvojiti vremena da vam pokažu zadatak koji ne znate rije&scaron;iti ili pojasniti ne&scaron;to &scaron;to vam je jasno kao i poljoprivreda Eskimu. U svakoj sekciji može se <strong>primijeniti</strong> ili <strong>pro&scaron;iriti znanje</strong> stečeno na fakultetu (čak i u planinarskoj) i skupiti hrpu prijatelja.</p><p><strong>Članarina</strong> u KSET-u iznosi 100 kn, uz članstvo dobijete mogućnost konzumiranja pića na &scaron;anku po <strong>studenskim cijenama</strong> (kava 3 kn, sok 3,5...), besplatne upade srijedom na različite slu&scaron;aonice i petkom na <strong>KSET Caffe</strong> (&scaron;to su najbolji tulumi nakon ispita). Čitajte dalje ili dođite petkom između 13 i 14h u Klub i saznajte sami...</p>",
				"text/html", "UTF-8", null);
		webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		return webview;
	}

	@Override
	public void onResume() {
		super.onResume();
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.about_kset_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.about_kset_open){
			startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(WEB_LOCATION)));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
