package com.imorih.common;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EFragment
public abstract class BaseFragment extends Fragment {
  public abstract interface BaseOnFragmentInteractionListener {
  }

  @UiThread
  protected void toast(String text) {
    if (getActivity() == null) {
      return;
    }
    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    Log.d("test", text);
  }

  protected void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
    }

  }

  protected abstract <T extends BaseOnFragmentInteractionListener> void setListener(T listener);

  protected abstract Class<? extends BaseOnFragmentInteractionListener> getListenerClass();

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    List<Type> types = new ArrayList<>();
    types.addAll(Arrays.asList(activity.getClass().getInterfaces()));
    types.addAll(Arrays.asList(activity.getClass().getSuperclass().getInterfaces()));
    String checkName = "interface " + getListenerClass().getCanonicalName();
    for (Type type : types) {
      String name = type.toString().replace('$', '.');
      if (!name.equals(checkName)) {
        continue;
      }
      setListener((BaseOnFragmentInteractionListener) activity);
      return;

    }
    throw new ClassCastException(activity.toString()
        + " must implement OnFragmentInteractionListener");
  }

  @Override
  public void onDetach() {
    super.onDetach();
    setListener(null);
  }


}
