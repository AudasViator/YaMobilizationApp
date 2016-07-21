package pro.audasviator.yamobilizationapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class AboutFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.about_title)
                .setMessage(R.string.about_message)
                .setPositiveButton(R.string.about_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "ne@napishesh.su", null));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Amazing artists app");
                        intent.putExtra(Intent.EXTRA_TEXT, "Why it don`t work?");
                        startActivity(Intent.createChooser(intent, getString(R.string.intent_chooser_title_send_email)));
                    }
                })
                .setNegativeButton(R.string.about_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
