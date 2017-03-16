package com.sugar.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sugar.myapplication.R;
import com.sugar.myapplication.util.ToastUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FeedbackActivity extends Activity {

    @InjectView(R.id.iv_back_feedback)
    ImageView mIvBackFeedback;
    @InjectView(R.id.gv_addpictrue_feedback)
    GridView mGvAddpictrueFeedback;

    private ArrayList<String> list = new ArrayList<>();
    private MGDadapter mMgDadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);


        mMgDadapter = new MGDadapter();
        mGvAddpictrueFeedback.setAdapter(mMgDadapter);

        mGvAddpictrueFeedback.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mGvAddpictrueFeedback.getCount() - 1) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                } else {
                    list.remove(position);
                    mMgDadapter.notifyDataSetChanged();
                }

            }
        });


    }


    @OnClick(R.id.iv_back_feedback)
    public void onClick(View view) {

        finish();

    }
    @OnClick(R.id.tv_submmit_feedback)
    public void onClick() {
        ToastUtil.showToast("提交成功");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            list.add(picturePath);

            mMgDadapter.notifyDataSetChanged();


        }

    }



    class MGDadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplication(), R.layout.item_grid_feedback, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_feedback_pic);
            if (position < list.size()) {

                imageView.setImageBitmap(BitmapFactory.decodeFile(list.get(position)));
            } else {
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageResource(R.drawable.add_erropicture);
            }
            return convertView;
        }
    }

}

