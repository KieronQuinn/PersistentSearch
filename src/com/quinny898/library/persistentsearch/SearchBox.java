package com.quinny898.library.persistentsearch;

import java.util.ArrayList;

import com.balysv.materialmenu.MaterialMenuView;
import com.balysv.materialmenu.MaterialMenuDrawable.IconState;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SearchBox extends RelativeLayout {

	private MaterialMenuView materialMenu;
	private TextView logo;
	private EditText search;
	private Context context;
	private ListView results;
	private ArrayList<SearchResult> resultList;
	private ArrayList<SearchResult> searchables;
	private boolean searchOpen;
	private boolean animate;
	private View tint;
	private boolean isMic;
	private ImageView mic;
	private SearchListener listener;
	private MenuListener menuListener;
	private FrameLayout rootLayout;
	private String logoText;

	public SearchBox(Context context) {
		this(context, null);
	}

	public SearchBox(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SearchBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		inflate(context, R.layout.searchbox, this);
		this.searchOpen = false;
		this.isMic = true;
		this.materialMenu = (MaterialMenuView) findViewById(R.id.material_menu_button);
		this.logo = (TextView) findViewById(R.id.logo);
		this.search = (EditText) findViewById(R.id.search);
		this.results = (ListView) findViewById(R.id.results);
		this.context = context;
		this.mic = (ImageView) findViewById(R.id.mic);
		materialMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (searchOpen) {
					toggleSearch();
				} else {
					if (menuListener != null)
						menuListener.onMenuClick();
				}
			}

		});
		resultList = new ArrayList<SearchResult>();
		results.setAdapter(new SearchAdapter(context, resultList));
		animate = true;
		logo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleSearch();
			}

		});
		RelativeLayout searchRoot = (RelativeLayout) findViewById(R.id.search_root);
		LayoutTransition lt = new LayoutTransition();
		lt.setDuration(100);
		searchRoot.setLayoutTransition(lt);
		searchables = new ArrayList<SearchResult>();
		search.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					search(getSearchText());
					return true;
				}
				return false;
			}
		});
		search.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if (TextUtils.isEmpty(getSearchText())) {
						toggleSearch();
					} else {
						search(getSearchText());
					}
					return true;
				}
				return false;
			}
		});
		logoText = "Logo";
	}

	private void search(String searchTerm) {
		setSearchString(searchTerm);
		if (!TextUtils.isEmpty(getSearchText())) {
			setLogoTextInt(searchTerm);
			if (listener != null)
				listener.onSearch(searchTerm);
		} else {
			setLogoTextInt(logoText);
		}
		toggleSearch();
	}

	public void setMenuListener(MenuListener menuListener) {
		this.menuListener = menuListener;
	}

	public void setSearchListener(SearchListener listener) {
		this.listener = listener;
	}

	public void toggleSearch() {
		if (searchOpen) {
			if (TextUtils.isEmpty(getSearchText())) {
				setLogoTextInt(logoText);
			}
			closeSearch();
		} else {
			openSearch(true);
		}
		searchOpen = !searchOpen;
	}

	private void openSearch(Boolean openKeyboard) {
		this.materialMenu.animateState(IconState.ARROW);
		this.logo.setVisibility(View.GONE);
		this.search.setVisibility(View.VISIBLE);
		this.search.requestFocus();
		this.results.setVisibility(View.VISIBLE);
		animate = true;
		results.setAdapter(new SearchAdapter(context, resultList));
		search.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					isMic = false;
					mic.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_clear));
				} else {
					isMic = true;
					mic.setImageDrawable(context.getResources().getDrawable(
							R.drawable.ic_action_mic));
				}
				updateResults();
				if (listener != null)
					listener.onSearchTermChanged();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

		});
		results.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SearchResult result = searchables.get(arg2);
				search(result.title);

			}

		});
		updateResults();
		if (listener != null)
			listener.onSearchOpened();
		if (getSearchText().length() > 0) {
			isMic = false;
			mic.setImageDrawable(context.getResources().getDrawable(
					R.drawable.ic_clear));
		}
		if (openKeyboard) {
			InputMethodManager inputMethodManager = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(
					getApplicationWindowToken(),
					InputMethodManager.SHOW_FORCED, 0);
		}
	}

	public void micClick(Activity activity) {
		if (!isMic) {
			this.setSearchString("");
		} else {
			startVoiceRecognitionActivity(activity);
		}

	}

	public void setMaxLength(int length) {
		search.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				length) });
	}

	private void closeSearch() {
		this.materialMenu.animateState(IconState.BURGER);
		this.logo.setVisibility(View.VISIBLE);
		this.search.setVisibility(View.GONE);
		this.results.setVisibility(View.GONE);
		if (tint != null && rootLayout != null) {
			rootLayout.removeView(tint);
		}
		if (listener != null)
			listener.onSearchClosed();
		isMic = true;
		mic.setImageDrawable(context.getResources().getDrawable(
				R.drawable.ic_action_mic));
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(),
				0);
	}
	

	public void updateResults() {
		resultList.clear();
		int count = 0;
		for (int x = 0; x < searchables.size(); x++) {
			if (searchables.get(x).title.toLowerCase().startsWith(getSearchText().toLowerCase())
					&& count < 5) {
				addResult(searchables.get(x));
				count++;
			}
		}
		if(resultList.size() == 0){
			results.setVisibility(View.GONE);
		}else{
			results.setVisibility(View.VISIBLE);
		}

	}

	public void setLogoText(String text) {
		this.logoText = text;
		setLogoTextInt(text);
	}

	private void setLogoTextInt(String text) {
		logo.setText(text);
	}

	public void startVoiceRecognitionActivity(Activity a) {
		if (a != null) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
					context.getString(R.string.speak_now));
			a.startActivityForResult(intent, 1234);
		}
	}

	public void populateEditText(ArrayList<String> matches) {
		toggleSearch();
		String text = "";
		for (int x = 0; x < matches.size(); x++) {
			text = text + matches.get(x) + " ";
		}
		text = text.trim();
		setSearchString(text);
		search(text);
	}

	public String getSearchText() {
		return search.getText().toString();
	}

	public void setSearchString(String text) {
		search.setText(text);
	}

	public void clearResults() {
		if (resultList != null) {
			resultList.clear();
			((SearchAdapter) results.getAdapter()).notifyDataSetChanged();
		}
	}

	public int getNumberOfResults() {
		return resultList.size();
	}

	private void addResult(SearchResult result) {
		if (resultList != null && resultList.size() < 6) {
			resultList.add(result);
			((SearchAdapter) results.getAdapter()).notifyDataSetChanged();
		}
	}

	public void addSearchable(SearchResult searchable) {
		if (!searchables.contains(searchable))
			searchables.add(searchable);
	}

	public void removeSearchable(SearchResult searchable) {
		if (searchables.contains(searchable))
			searchables.remove(search);
	}

	public void clearSearchable() {
		searchables.clear();
	}

	public ArrayList<SearchResult> getSearchables() {
		return searchables;
	}

	class SearchAdapter extends ArrayAdapter<SearchResult> {
		public SearchAdapter(Context context, ArrayList<SearchResult> options) {
			super(context, 0, options);
		}

		int count = 0;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			SearchResult option = getItem(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.search_option, parent, false);

				if (animate) {
					Animation anim = AnimationUtils.loadAnimation(context,
							R.anim.anim_down);
					anim.setDuration(400);
					convertView.startAnimation(anim);
					if (count == this.getCount()) {
						animate = false;
					}
					count++;
				}
			}

			View border = convertView.findViewById(R.id.border);
			if (position == 0) {
				border.setVisibility(View.VISIBLE);
			} else {
				border.setVisibility(View.GONE);
			}
			final TextView title = (TextView) convertView
					.findViewById(R.id.title);
			title.setText(option.title);
			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			icon.setImageDrawable(option.icon);
			ImageView up = (ImageView) convertView.findViewById(R.id.up);
			up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setSearchString(title.getText().toString());
					search.setSelection(search.getText().length());
				}

			});

			return convertView;
		}
	}

	public interface SearchListener {
		public void onSearchOpened();

		public void onSearchClosed();

		public void onSearchTermChanged();

		public void onSearch(String searchTerm);
	}

	public interface MenuListener {
		public void onMenuClick();
	}

}
