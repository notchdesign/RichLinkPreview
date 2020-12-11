package io.github.ponnamkarthik.richlinkpreview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ponna on 16-01-2018.
 */

public class RichLinkView extends RelativeLayout {

    private View view;
    Context context;
    private MetaData meta;

    LinearLayout linearLayout;
    ImageView imageView;
    TextView textViewTitle;
    TextView textViewDesp;
    TextView textViewUrl;

    private String main_url;

    private boolean isDefaultClick = true;

    private RichLinkListener richLinkListener;


    public RichLinkView(Context context) {
        super(context);
        this.context = context;
    }

    public RichLinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RichLinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RichLinkView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


public void initView() {
    this.view = this;
    inflate(context, R.layout.include_link_preview_post_item,this);
    linearLayout = (LinearLayout) findViewById(R.id.llMainIncRowLinPreviewPost);
    imageView = (AppCompatImageView) findViewById(R.id.ivPreviewIncRowLinPreviewPost);
    textViewTitle = (AppCompatTextView) findViewById(R.id.tvTitleIncRowLinPreviewPost);
    textViewUrl = (AppCompatTextView) findViewById(R.id.tvLinkIncRowLinPreviewPost);
  }

private void setData(){
    if(meta.getImageurl().equals("") || meta.getImageurl().isEmpty()) {
        imageView.setVisibility(GONE);
    } else {
        imageView.setVisibility(VISIBLE);
        Glide.with(context).load(meta.getImageurl()).into(imageView);
    }

    if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
        textViewTitle.setVisibility(GONE);
    } else {
        textViewTitle.setVisibility(VISIBLE);
        textViewTitle.setText(meta.getTitle());
    }
    if(meta.getUrl().isEmpty() || meta.getUrl().equals("")) {
        textViewUrl.setVisibility(GONE);
    } else {
        textViewUrl.setVisibility(VISIBLE);
        textViewUrl.setText(meta.getUrl());
    }

    linearLayout.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isDefaultClick) {
                richLinkClicked();
            } else {
                if(richLinkListener != null) {
                    richLinkListener.onClicked(view, meta);
                } else {
                    richLinkClicked();
                }
            }
        }
    });
}


    private void richLinkClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
        context.startActivity(intent);
    }


    public void setDefaultClickListener(boolean isDefault) {
        isDefaultClick = isDefault;
    }

    public void setClickListener(RichLinkListener richLinkListener1) {
        richLinkListener = richLinkListener1;
    }

    protected LinearLayout findLinearLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (LinearLayout) getChildAt(0);
        }
        return null;
    }

    public void setLinkFromMeta(MetaData metaData) {
        meta = metaData;
        initView();
    }

    public MetaData getMetaData() {
        return meta;
    }

    public void setLink(String url, final ViewListener viewListener) {
        main_url = url;
        RichPreview richPreview = new RichPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                meta = metaData;
                if(!meta.getTitle().isEmpty() || !meta.getTitle().equals("")) {
                    viewListener.onSuccess(true);
                }
                
                setData();
            }

            @Override
            public void onError(Exception e) {
                viewListener.onError(e);
            }
        });
        richPreview.getPreview(url);
    }

}
