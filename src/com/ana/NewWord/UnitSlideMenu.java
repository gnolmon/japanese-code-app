package com.ana.NewWord;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.japaneseapp.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class UnitSlideMenu extends SlidingMenu {
	Activity activity;
	private ListView listNameUnit;
	private ListUnitAdapter listUnitAdapter;
	private int idUnit = 0;
	private TextView tv_Unit;

	public UnitSlideMenu(Activity activity) {
		super(activity);
		setFadeDegree(0.35f);
		this.activity = activity;
		setMenu(R.layout.leftside_unit_pane);

		attachToActivity(activity, SLIDING_WINDOW);
		listNameUnit = (ListView) findViewById(R.id.listUnit);
		listUnitAdapter = new ListUnitAdapter(activity,
				R.layout.item_unit_layout);
		listNameUnit.setAdapter(listUnitAdapter);

		tv_Unit = (TextView) findViewById(R.id.btnTestNewWord1);
		listNameUnit
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						idUnit = position;
						tv_Unit.setText("Unit " + (position + 1));
						((UnitChangeListener) UnitSlideMenu.this.activity)
								.onChangeUnit(position);
						showContent();
					}
				});

	}

	public interface UnitChangeListener {
		public void onChangeUnit(int idUnit);
	}

	public class ListUnitAdapter extends ArrayAdapter<String> {
		private Activity context = null;
		private int layoutId;

		public ListUnitAdapter(Activity context, int layoutId) {
			super(context, layoutId);
			this.context = context;
			this.layoutId = layoutId;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View listView;
			if (convertView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				listView = inflater.inflate(R.layout.item_unit_layout, null);
			} else {
				listView = convertView;
			}
			TextView tvNameOfUnit = (TextView) listView
					.findViewById(R.id.nameOfUnit);
			String name = "Bài học " + (position + 1);
			tvNameOfUnit.setText(name);
			return listView;
		}

		@Override
		public int getCount() {
			return NameUnit.length;
		}

		public int NameUnit[] = { R.raw.unit1, R.raw.unit2, R.raw.unit3,
				R.raw.unit4, R.raw.unit5, R.raw.unit6, R.raw.unit7,
				R.raw.unit8, R.raw.unit9, R.raw.unit10, R.raw.unit11,
				R.raw.unit12, R.raw.unit13, R.raw.unit14, R.raw.unit15,
				R.raw.unit16, R.raw.unit17, R.raw.unit18, R.raw.unit19,
				R.raw.unit20, R.raw.unit21, R.raw.unit22, R.raw.unit23,
				R.raw.unit24, R.raw.unit25 };

	}

}
