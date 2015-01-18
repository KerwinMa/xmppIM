package quinn.xmpp.activity.laucher;

import quinn.xmpp.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StartFragment extends Fragment{
	private Button loginButton;
	private Button signUpButton;
	private ViewGroup rootView;
	private LoginListener loginListener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.login_fragment, container, false);
		this.rootView = rootView;
		loginButton = (Button) rootView.findViewById(R.id.Login_loginButton);
		signUpButton = (Button) rootView.findViewById(R.id.Login_SignUpButton);
		loginListener = new LoginListener(this.getActivity());
		regist(loginButton);
		regist(signUpButton);

		return rootView;
	}
	public void regist(View view){
		loginListener.add(view);
	}

	
}
