package cz.master.extern.babyradio.ratioresolver;

import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomViewHandler {

	View target_view;

	public final int LINEAR_LAYOUT = 1;
	public final int FRAME_LAYOUT = 2;
	public final int RELATIVE_LAYOUT = 3;
	public int PARENT_LAYOUT;

	private LinearLayout.LayoutParams linear_layout_params;
	private FrameLayout.LayoutParams frame_layout_params;
	private RelativeLayout.LayoutParams relative_layout_params;

	public double WIDTH_RATIO;
	public double HEIGHT_RATIO;

	public CustomViewHandler(View target_view) {

		this.target_view = target_view;
		View parent = (View) target_view.getParent();

		if (parent instanceof LinearLayout) {
			PARENT_LAYOUT = LINEAR_LAYOUT;
			linear_layout_params = (LinearLayout.LayoutParams) target_view
					.getLayoutParams();
		}
		if (parent instanceof FrameLayout) {
			PARENT_LAYOUT = FRAME_LAYOUT;
			frame_layout_params = (FrameLayout.LayoutParams) target_view
					.getLayoutParams();
		}
		if (parent instanceof RelativeLayout) {
			PARENT_LAYOUT = RELATIVE_LAYOUT;
			relative_layout_params = (RelativeLayout.LayoutParams) target_view
					.getLayoutParams();
		}

		WIDTH_RATIO = CalculateRatio.WIDTH_RATIO;
		HEIGHT_RATIO = CalculateRatio.HEIGHT_RATIO;

	}

	public void setWidth(int width) {


		target_view.getLayoutParams().width = (int) (width * WIDTH_RATIO);
	}

	public int getWidth() {
		return target_view.getLayoutParams().width;
	}

	public void setHeight(int height) {
		target_view.getLayoutParams().height = height;
		target_view.getLayoutParams().height = (int) (height * HEIGHT_RATIO);
	}

	public int getHeight() {
		return target_view.getLayoutParams().height;
	}

	public void setMargin(int left, int top, int right, int bottom) {

		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			linear_layout_params.setMargins((int) (left * WIDTH_RATIO),
					(int) (top * HEIGHT_RATIO), (int) (right * WIDTH_RATIO),
					(int) (bottom * HEIGHT_RATIO));
			target_view.setLayoutParams(linear_layout_params);
			break;
		case FRAME_LAYOUT:
			frame_layout_params.setMargins((int) (left * WIDTH_RATIO),
					(int) (top * HEIGHT_RATIO), (int) (right * WIDTH_RATIO),
					(int) (bottom * HEIGHT_RATIO));
			target_view.setLayoutParams(frame_layout_params);
			break;
		case RELATIVE_LAYOUT:
			relative_layout_params.setMargins((int) (left * WIDTH_RATIO),
					(int) (top * HEIGHT_RATIO), (int) (right * WIDTH_RATIO),
					(int) (bottom * HEIGHT_RATIO));
			target_view.setLayoutParams(relative_layout_params);
			break;
		}
	}

	public void setMarginLeft(int left) {

		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			linear_layout_params.leftMargin = (int) (left * WIDTH_RATIO);
			target_view.setLayoutParams(linear_layout_params);
			break;
		case FRAME_LAYOUT:
			frame_layout_params.leftMargin = (int) (left * WIDTH_RATIO);
			target_view.setLayoutParams(frame_layout_params);
			break;
		case RELATIVE_LAYOUT:
			relative_layout_params.leftMargin = (int) (left * WIDTH_RATIO);
			target_view.setLayoutParams(relative_layout_params);
			break;
		}

	}

	public int getMarginLeft() {
		int margin = 0;
		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			margin = linear_layout_params.leftMargin;
			break;
		case FRAME_LAYOUT:
			margin = frame_layout_params.leftMargin;
			break;
		case RELATIVE_LAYOUT:
			margin = relative_layout_params.leftMargin;
			break;
		}
		return margin;
	}

	public void setMarginTop(int top) {

		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			linear_layout_params.topMargin = (int) (top * HEIGHT_RATIO);
			target_view.setLayoutParams(linear_layout_params);
			break;
		case FRAME_LAYOUT:
			frame_layout_params.topMargin = (int) (top * HEIGHT_RATIO);
			target_view.setLayoutParams(frame_layout_params);
			break;
		case RELATIVE_LAYOUT:
			relative_layout_params.topMargin = (int) (top * HEIGHT_RATIO);
			target_view.setLayoutParams(relative_layout_params);
			break;
		}

	}

	public int getMarginTop() {
		int margin = 0;
		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			margin = linear_layout_params.topMargin;
			break;
		case FRAME_LAYOUT:
			margin = frame_layout_params.topMargin;
			break;
		case RELATIVE_LAYOUT:
			margin = relative_layout_params.topMargin;
			break;
		}
		return margin;
	}

	public void setMarginRight(int right) {

		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			linear_layout_params.rightMargin = (int) (right * WIDTH_RATIO);
			target_view.setLayoutParams(linear_layout_params);
			break;
		case FRAME_LAYOUT:
			frame_layout_params.rightMargin = (int) (right * WIDTH_RATIO);
			target_view.setLayoutParams(frame_layout_params);
			break;
		case RELATIVE_LAYOUT:
			relative_layout_params.rightMargin = (int) (right * WIDTH_RATIO);
			target_view.setLayoutParams(relative_layout_params);
			break;
		}

	}

	public int getMarginRight() {
		int margin = 0;
		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			margin = linear_layout_params.rightMargin;
			break;
		case FRAME_LAYOUT:
			margin = frame_layout_params.rightMargin;
			break;
		case RELATIVE_LAYOUT:
			margin = relative_layout_params.rightMargin;
			break;
		}
		return margin;
	}

	public void setMarginBottom(int bottom) {

		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			linear_layout_params.bottomMargin = (int) (bottom * HEIGHT_RATIO);
			target_view.setLayoutParams(linear_layout_params);
			break;
		case FRAME_LAYOUT:
			frame_layout_params.bottomMargin = (int) (bottom * HEIGHT_RATIO);
			target_view.setLayoutParams(frame_layout_params);
			break;
		case RELATIVE_LAYOUT:
			relative_layout_params.bottomMargin = (int) (bottom * HEIGHT_RATIO);
			target_view.setLayoutParams(relative_layout_params);
			break;
		}

	}

	public int getMarginBottom() {
		int margin = 0;
		switch (PARENT_LAYOUT) {
		case LINEAR_LAYOUT:
			margin = linear_layout_params.bottomMargin;
			break;
		case FRAME_LAYOUT:
			margin = frame_layout_params.bottomMargin;
			break;
		case RELATIVE_LAYOUT:
			margin = relative_layout_params.bottomMargin;
			break;
		}
		return margin;
	}

	public void setPadding(int left, int top, int right, int bottom) {
		target_view.setPadding((int) (left * WIDTH_RATIO),
				(int) (top * HEIGHT_RATIO), (int) (right * WIDTH_RATIO),
				(int) (bottom * HEIGHT_RATIO));
	}

	public void setPaddingLeft(int left) {
		target_view.setPadding((int) (left * WIDTH_RATIO),
				target_view.getPaddingTop(), target_view.getPaddingRight(),
				target_view.getPaddingBottom());
	}

	public void setPaddingTop(int top) {
		target_view.setPadding(target_view.getPaddingLeft(),
				(int) (top * HEIGHT_RATIO), target_view.getPaddingRight(),
				target_view.getPaddingBottom());
	}

	public void setPaddingRight(int right) {
		target_view.setPadding(target_view.getPaddingLeft(),
				target_view.getPaddingTop(), (int) (right * WIDTH_RATIO),
				target_view.getPaddingBottom());
	}

	public void setPaddingBottom(int bottom) {
		target_view.setPadding(target_view.getPaddingLeft(),
				target_view.getPaddingTop(), target_view.getPaddingRight(),
				(int) (bottom * HEIGHT_RATIO));
	}

	public void setTextSize(int size) {
		((TextView) target_view).setTextSize(TypedValue.COMPLEX_UNIT_PX,
				(float) (size * CalculateRatio.WIDTH_RATIO));
	}

}
