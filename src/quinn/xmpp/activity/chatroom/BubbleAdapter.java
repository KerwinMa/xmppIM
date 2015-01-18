package quinn.xmpp.activity.chatroom;

import java.util.ArrayList;

import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

import quinn.xmpp.R;
import quinn.xmpp.model.BubbleMessage;
import quinn.xmpp.smack.SmackImpl;
import quinn.xmpp.utils.Constants;
import quinn.xmpp.utils.MessageType;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atermenji.android.iconicdroid.IconicFontDrawable;
import com.atermenji.android.iconicdroid.icon.EntypoIcon;
import com.rockerhieu.emojicon.EmojiconTextView;

/**
 * AwesomeAdapter is a Custom class to implement custom row in ListView
 * 
 * @author Adil Soomro
 * 
 */
public class BubbleAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BubbleMessage> mMessages;
	private BubbleMessage positonMessage;
	private final static int ITEM_VIEW_TYPE = 4;
	private String u_jid;
	private FileTransferRequest currentClickFileRequest;

	public BubbleAdapter(Context context, String u_jid) {
		super();
		this.u_jid = u_jid;
		this.mContext = context;
		this.mMessages = SmackImpl.getInstance().getBubbleList(u_jid);
	}

	@Override
	public int getCount() {
		return mMessages.size();
	}

	@Override
	public Object getItem(int position) {
		return mMessages.get(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return ITEM_VIEW_TYPE;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		positonMessage = (BubbleMessage) this.getItem(position);

		//
		switch (getItemViewType(position)) {

		case 0:
			convertView = getTimeMessage(position, convertView, parent);
			return convertView;
		case 1:
			convertView = getTextMessage(position, convertView, parent);
			return convertView;
		case 2:
			convertView = getFileMessage(position, convertView, parent);
			return convertView;
		case 3:
			convertView = getSoundMessage(position, convertView, parent);
			return convertView;
		}

		return convertView;
	}

	public View getSoundMessage(final int position, View convertView,
			ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			if (positonMessage.isMine()) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.bubble_sound, parent, false);
				holder.itemArea = (View) convertView
						.findViewById(R.id.soundArea);
				holder.image = (ImageView) convertView
						.findViewById(R.id.soundIcon);
				holder.fileStage = (TextView) convertView
						.findViewById(R.id.soundStage);
				holder.progressBar = (ProgressBar) convertView
						.findViewById(R.id.soundProgress);
			} else {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.bubble_sound_left, parent, false);
				holder.itemArea = (View) convertView
						.findViewById(R.id.soundArea_left);
				holder.image = (ImageView) convertView
						.findViewById(R.id.soundIcon_left);
				holder.fileStage = (TextView) convertView
						.findViewById(R.id.soundStage_left);
				holder.progressBar = (ProgressBar) convertView
						.findViewById(R.id.soundProgress_left);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		IconicFontDrawable icon_sound = new IconicFontDrawable(this.mContext);
		if (positonMessage.isMine()) {
			icon_sound.setIcon(EntypoIcon.TRIANGLE_LEFT);
		} else {
			icon_sound.setIcon(EntypoIcon.TRIANGLE_RIGHT);
		}
		icon_sound.setIconColor(Constants.COLOR_COMMON_BLUE);
		holder.image.setBackground(icon_sound);

		int minuteVal = positonMessage.getSumSecond() / 60;
		int secondVal = positonMessage.getSumSecond() % 60;
		String timeLength = "";
		if (minuteVal != 0) {
			timeLength = minuteVal + "\"";
		} else {
			timeLength = timeLength + secondVal + "'";
		}
		holder.fileStage.setText(timeLength);

		convertView.setOnClickListener(new OnClickListener() {
			String path = positonMessage.getPath();

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SoundPlayer(mContext, u_jid, position,
						positonMessage.getSumSecond(), path).click();

			}
		});

		final int val = positonMessage.getFileProgressVal();
		if (val > 0 && val <= 100) {
			holder.progressBar.setProgress(val);
			positonMessage.setFileProgressVal(val);
		}

		LayoutParams lp = (LayoutParams) holder.itemArea.getLayoutParams();

		if (positonMessage.isMine()) {
			holder.itemArea.setBackgroundResource(R.drawable.bubble_right);
			lp.gravity = Gravity.RIGHT;
		} else {
			holder.itemArea.setBackgroundResource(R.drawable.bubble_left);
			lp.gravity = Gravity.LEFT;
		}

		return convertView;
	}

	public View getFileMessage(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bubble_file, parent, false);
			holder.itemArea = (View) convertView.findViewById(R.id.fileArea);
			holder.filename = (TextView) convertView
					.findViewById(R.id.filename);
			holder.filesize = (TextView) convertView
					.findViewById(R.id.filesize);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressFile);
			holder.fileStage = (TextView) convertView
					.findViewById(R.id.fileStage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.filename.setText(positonMessage.getFilename());
		holder.filesize.setText(positonMessage.getFilesize());
		holder.fileStage.setText(positonMessage.getFileStage());
		final int val = positonMessage.getFileProgressVal();

		// convertView.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// new SoundPlayer(positonMessage.getPath()).click();
		// }
		// });

		if (val > 0 && val < 100) {
			holder.progressBar.setVisibility(View.VISIBLE);
			holder.progressBar.setProgress(val);
			holder.fileStage.setText(val + "%");
			positonMessage.setFileStage(val + "%");
		} else {
			holder.progressBar.setVisibility(View.GONE);
		}

		LayoutParams lp = (LayoutParams) holder.itemArea.getLayoutParams();

		if (positonMessage.isMine()) {
			holder.itemArea.setBackgroundResource(R.drawable.bubble_right);
			lp.gravity = Gravity.RIGHT;
		} else {
			final int currentPosition = position;
			currentClickFileRequest = positonMessage.getRequest();

			if (positonMessage.getFileStage().equals(
					AsyncTaskContants.STR_NEGOTIATING)) {
				convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((ChatRoomActivity) mContext)
								.showReceiveChocieFragment(currentPosition);
					}
				});
			} else {
				convertView.setOnClickListener(null);
			}
			holder.itemArea.setBackgroundResource(R.drawable.bubble_left);
			lp.gravity = Gravity.LEFT;

		}
		holder.itemArea.setLayoutParams(lp);
		return convertView;

	}

	public View getTimeMessage(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bubble_text, parent, false);
			holder.time = (EmojiconTextView) convertView
					.findViewById(R.id.message_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.time.setText(positonMessage.getMessage());
		holder.time.setTextSize(mContext.getResources().getDimension(
				R.dimen.bubbleTTextsizeTime));
		holder.time.setMinimumHeight(10);
		LayoutParams lp = (LayoutParams) holder.time.getLayoutParams();
		lp.gravity = Gravity.CENTER;
		holder.time.setLayoutParams(lp);
		return convertView;
	}

	public View getTextMessage(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.bubble_text, parent, false);
			holder.message = (EmojiconTextView) convertView
					.findViewById(R.id.message_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.message.setText(positonMessage.getMessage());
		holder.message.setTextSize(mContext.getResources().getDimension(
				R.dimen.bubbleTTextsizeText));

		LayoutParams lp = (LayoutParams) holder.message.getLayoutParams();

		if (positonMessage.isMine()) {
			holder.message.setBackgroundResource(R.drawable.bubble_right);
			lp.gravity = Gravity.RIGHT;
		} else {
			holder.message.setBackgroundResource(R.drawable.bubble_left);
			lp.gravity = Gravity.LEFT;

		}
		holder.message.setLayoutParams(lp);
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		positonMessage = (BubbleMessage) this.getItem(position);
		if (positonMessage.getType() == MessageType.TIME)
			return 0;
		if (positonMessage.getType() == MessageType.TEXT)
			return 1;
		if (positonMessage.getType() == MessageType.FILE)
			return 2;
		if (positonMessage.getType() == MessageType.SOUND)
			return 3;
		return 1;
	}

	private class ViewHolder {
		EmojiconTextView message;
		EmojiconTextView time;

		View itemArea;
		TextView filename;
		TextView filesize;
		TextView fileStage;
		ProgressBar progressBar;

		ImageView image;

	}

	@Override
	public long getItemId(int position) {
		// Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
