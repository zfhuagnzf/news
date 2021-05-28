package com.ant.core.mvvm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ant.core.BaseApplication;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragment<DATA_BINDING extends ViewDataBinding,
        VIEW_MODEL extends ViewModel> extends Fragment {

    protected DATA_BINDING viewDataBinding;
    protected VIEW_MODEL viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false);
        createViewModel();
        init();
        return viewDataBinding.getRoot();
    }

    protected void createViewModel() {
        Class modelClass = null;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] cls = ((ParameterizedType) type).getActualTypeArguments();
            if (cls.length == 2) {
                modelClass = (Class) cls[1];
            }
        }
        if (modelClass == null) {
            //如果没有指定泛型参数，则默认使用NormalViewModel
            modelClass = NormalViewModel.class;
        }
        viewModel = (VIEW_MODEL) new ViewModelProvider(getActivity(),
                new SavedStateViewModelFactory(getActivity().getApplication(), this, getArguments()))
                .get(modelClass);
    }

    protected abstract int layoutId();

    protected abstract void init();

}