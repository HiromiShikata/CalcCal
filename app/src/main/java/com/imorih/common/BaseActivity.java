package com.imorih.common;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

@EActivity
public abstract class BaseActivity extends AppCompatActivity {

  protected void showFragment(Fragment f) {
    showFragment(f, android.R.id.content);
  }

  protected void showFragment(Fragment f, int contentId) {
    getFragmentManager()
        .beginTransaction()
        .add(contentId, f, f.getClass().getName())
        .addToBackStack(null)
        .commit();
  }

  @UiThread
  protected void toast(String text) {
    Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();

  }

  protected void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      //noop
    }
  }

}
