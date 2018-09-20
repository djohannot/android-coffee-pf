package com.ysdc.coffee.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.ui.base.BaseFragment;
import com.ysdc.coffee.ui.utils.MenuDisplayer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderFragment extends BaseFragment implements OrderMvpView, MenuDisplayer {

    private Integer menuId;
    private CompositeDisposable compositeDisposable;
    private ProductAdapter adapter;

    @Inject
    OrderMvpPresenter<OrderMvpView> presenter;
    @BindView(R.id.product_list)
    protected RecyclerView productList;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        getFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(OrderFragment.this);
        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    @Override
    protected void setUp(View view) {
        compositeDisposable = new CompositeDisposable();

        productList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        productList.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadProducts();
        });

        loadProducts();
    }

    private void loadProducts() {
        compositeDisposable.add(presenter.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(products -> {
                    if (products.isEmpty()) {
                        showProducts(products);
                    }
                }, throwable -> onError(throwable))
        );
    }

    @Override
    public boolean shouldToolbarBeElevated() {
        return false;
    }

    @Override
    public String getCustomTitle() {
        return getString(R.string.title_order);
    }

    @Override
    public int getMenu() {
        if (menuId != null) {
            return menuId;
        } else {
            return R.menu.menu_empty;
        }
    }

    @Override
    public void showMenu(Integer menuId) {
        this.menuId = menuId;
        if (getBaseActivity() != null) {
            getBaseActivity().supportInvalidateOptionsMenu();
        }
    }
    private void showProducts(List<Product> properties) {
        if (adapter == null) {
            adapter = new ProductAdapter(properties, this::showCustomizeProduct, getActivity());
            productList.setAdapter(adapter);
        } else {
            adapter.updateProperties(properties);
        }
        adapter.notifyDataSetChanged();
    }

    private void showCustomizeProduct(Product product) {
        //TODO
    }
}
