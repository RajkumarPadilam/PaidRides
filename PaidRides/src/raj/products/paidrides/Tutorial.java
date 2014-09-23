package raj.products.paidrides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Tutorial extends Activity {

	Button Skip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		Skip=(Button)findViewById(R.id.skipTutorial);
		
		Skip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i=new Intent(Tutorial.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

}
