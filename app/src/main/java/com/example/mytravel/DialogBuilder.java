package com.example.mytravel;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogBuilder extends MainActivity implements View.OnClickListener {
    private AlertDialog dialog;
    private TextView roomCounter, adultCounter, childrenCounter;
    private final LayoutInflater layoutInflater;
    private final TextView setRoomsTxt;
    private final TextView setAdultsTxt;
    private final TextView setChildTxt;
    public DialogBuilder(LayoutInflater layoutInflater, TextView setRoomsTxt, TextView setAdultsTxt, TextView setChildTxt) {
    this.layoutInflater = layoutInflater;
    this.setAdultsTxt = setAdultsTxt;
    this.setRoomsTxt = setRoomsTxt;
    this.setChildTxt = setChildTxt;
    }

    public void createNewDialogContent() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View settingsPopupView = layoutInflater.inflate(R.layout.popup, null);
        Button buttonPlusAdult = settingsPopupView.findViewById(R.id.buttonPlusAdult);
        buttonPlusAdult.setOnClickListener(this);

        Button buttonPlusRoom = settingsPopupView.findViewById(R.id.buttonPlusRoom);
        buttonPlusRoom.setOnClickListener(this);

        Button buttonPlusChildren = settingsPopupView.findViewById(R.id.buttonPlusChildren);
        buttonPlusChildren.setOnClickListener(this);

        Button buttonMinusAdult = settingsPopupView.findViewById(R.id.buttonMinusAdult);
        buttonMinusAdult.setOnClickListener(this);

        Button buttonMinusRoom = settingsPopupView.findViewById(R.id.buttonMinusRoom);
        buttonMinusRoom.setOnClickListener(this);

        Button buttonMinusChildren = settingsPopupView.findViewById(R.id.buttonMinusChildren);
        buttonMinusChildren.setOnClickListener(this);

        Button buttonCancel = settingsPopupView.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(this);
        Button buttonApply = settingsPopupView.findViewById(R.id.buttonApply);
        buttonApply.setOnClickListener(this);

        roomCounter = settingsPopupView.findViewById(R.id.roomCounter);
        adultCounter = settingsPopupView.findViewById(R.id.adultCounter);
        childrenCounter = settingsPopupView.findViewById(R.id.childrenCounter);

        dialogBuilder.setView(settingsPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int countRoom = Integer.parseInt(roomCounter.getText().toString());
        int countAdult = Integer.parseInt(adultCounter.getText().toString());
        int countChildren = Integer.parseInt(childrenCounter.getText().toString());
        switch (view.getId()) {
            case R.id.buttonMinusRoom:
                if (countRoom > 1) {
                    countRoom--;
                    roomCounter.setText(String.valueOf(countRoom));
                }
                break;
            case R.id.buttonPlusRoom:
                countRoom++;
                if (countRoom > countAdult) {
                    countAdult++;
                    adultCounter.setText(String.valueOf(countAdult));
                }
                roomCounter.setText(String.valueOf(countRoom));
                break;
            case R.id.buttonMinusAdult:
                if (countAdult > 1) {
                    countAdult--;
                    if (countAdult < countRoom) {
                        countRoom--;
                        roomCounter.setText(String.valueOf(countRoom));
                    }
                    adultCounter.setText(String.valueOf(countAdult));
                }
                break;
            case R.id.buttonPlusAdult:
                countAdult++;
                adultCounter.setText(String.valueOf(countAdult));
                break;
            case R.id.buttonMinusChildren:
                if (countChildren > 0) {
                    countChildren--;
                    childrenCounter.setText(String.valueOf(countChildren));
                }
                break;
            case R.id.buttonPlusChildren:
                countChildren++;
                childrenCounter.setText(String.valueOf(countChildren));
                break;
            case R.id.buttonCancel:
                dialog.dismiss();
                break;
            case R.id.buttonApply:
                if (countRoom == 1) {
                    setRoomsTxt.setText(countRoom + " pokój");
                } else if (countRoom > 1 && countRoom < 5) {
                    setRoomsTxt.setText(countRoom + " pokoje");
                } else {
                    setRoomsTxt.setText(countRoom + " pokoi");
                }
                if (countAdult == 1) {
                    setAdultsTxt.setText(countAdult + " dorosły");
                } else {
                    setAdultsTxt.setText(countAdult + " dorosłych");
                }
                if (countChildren == 1) {
                    setChildTxt.setText(countChildren + " dziecko");
                } else {
                    setChildTxt.setText(countChildren + " dzieci");
                }
                dialog.dismiss();
                break;
        }
    }
}
