package com.example.jsonz.autopopwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView approvalReject;
    TextView approvalMore;
    TextView approvalAgree;
    CustomPopWindow popWindow;
    ArrowLinearLayout arrowLinearLayout;
    private boolean isAddApprove = true;
    private boolean isTransferApprove = true;
    private boolean isTransferView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        approvalReject = (TextView) findViewById(R.id.approvalReject);
        approvalAgree = (TextView) findViewById(R.id.approvalAgree);
        approvalMore = (TextView) findViewById(R.id.approvalMore);
        initMoreBtn();
        approvalMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignPop();
            }
        });
    }

    private void initMoreBtn() {

        if (popWindow == null) {
            popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(R.layout.af_pop_add_sign)
                    .setPopWindowListener(new CustomPopWindow.CustomPopWindowListener() {
                        @Override
                        public void onCreateView(View view) {
                            arrowLinearLayout = (ArrowLinearLayout) view.findViewById(R.id.popRoot);
//                            private boolean isAddApprove;//是否允许加签
//                            private boolean isTransferApprove;//是否允许转签
//                            private boolean isTransferView;//是否允许转阅
                            if (!isAddApprove) {
                                view.findViewById(R.id.btnAddSignBefore).setVisibility(View.GONE);
                                view.findViewById(R.id.btnAddSignAfter).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.btnAddSignBefore).setOnClickListener(MainActivity.this);
                                view.findViewById(R.id.btnAddSignAfter).setOnClickListener(MainActivity.this);
                            }
                            if (!isTransferApprove) {
                                view.findViewById(R.id.btnTransferSign).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.btnTransferSign).setOnClickListener(MainActivity.this);
                            }
                            if (!isTransferView) {
                                view.findViewById(R.id.btnTransferRead).setVisibility(View.GONE);
                            } else {
                                view.findViewById(R.id.btnTransferRead).setOnClickListener(MainActivity.this);
                            }
                        }
                    })
                    .setOutsideTouchable(true)
                    .create();
        }

    }

    private void showSignPop() {
        if (popWindow != null) {
            int parentH = approvalMore.getHeight();
            int parentW = approvalMore.getWidth();
            int popH = popWindow.getHeight();
            int popW = popWindow.getWidth();
            int margin = getResources().getDimensionPixelSize(R.dimen.cMarginListItem);
            int loc[] = new int[2];
            approvalMore.getLocationOnScreen(loc);
            arrowLinearLayout.setCx(loc[0] + parentW / 2);
//            popWindow.showAsDropDown(approvalMore, parentW - popW, -(parentH + popH + margin));
            popWindow.showAsDropDown(approvalMore, parentW - popW, (parentH));
        }
    }


    @Override
    public void onClick(View v) {

    }
}
