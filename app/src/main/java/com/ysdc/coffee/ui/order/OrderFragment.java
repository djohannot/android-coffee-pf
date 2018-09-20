package com.ysdc.coffee.ui.order;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.ui.base.BaseFragment;
import com.ysdc.coffee.ui.create.CreateOrderFragment;
import com.ysdc.coffee.ui.utils.MenuDisplayer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

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
    @BindView(R.id.send_layout)
    protected RelativeLayout send_Layout;
    @BindView(R.id.quantity_field)
    protected TextView quantityField;
    @BindView(R.id.order_header_filled)
    protected RelativeLayout headerFilled;
    @BindView(R.id.order_header_empty)
    protected RelativeLayout headerEmpty;

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
        refreshSendLayout();

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
                    if (!products.isEmpty()) {
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
        return EMPTY_STRING;
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

    private void showProducts(List<Product> products) {
        if (adapter == null) {
            adapter = new ProductAdapter(products, presenter.getOrderEntries(), this::showCustomizeProduct, getActivity());
            productList.setAdapter(adapter);
        } else {
            adapter.updateProducts(products);
        }
        adapter.notifyDataSetChanged();
    }

    private void showCustomizeProduct(Product product) {
        OrderEntry orderedProduct = presenter.getOrderEntriesForProduct(product);
        CreateOrderFragment createOrderFragment = CreateOrderFragment.newInstance(new CreateOrderFragment.CreateListener() {
            @Override
            public void onAddOrderPressed() {
                adapter.updateProductsQuantities(presenter.getOrderEntries());
                adapter.notifyDataSetChanged();
                refreshSendLayout();
            }
        }, orderedProduct);
        createOrderFragment.show(getActivity().getSupportFragmentManager(), createOrderFragment.getTag());
    }

    private void refreshSendLayout() {
        if (presenter.getOrderEntries().isEmpty()) {
            send_Layout.setVisibility(View.GONE);
            headerEmpty.setVisibility(View.VISIBLE);
            headerFilled.setVisibility(View.GONE);
        } else {
            send_Layout.setVisibility(View.VISIBLE);
            headerEmpty.setVisibility(View.GONE);
            headerFilled.setVisibility(View.VISIBLE);
            int quantity = 0;
            for(OrderEntry entry : presenter.getOrderEntries()){
                quantity += entry.getQuantity();
            }
            quantityField.setText(String.valueOf(quantity));
        }
    }

    @OnClick(R.id.send_btn)
    public void onSendClicked() {
        compositeDisposable.add(presenter.sendOrder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
                    orderSent();
                    presenter.cleanCurrentOrder();
                    adapter.updateProductsQuantities(presenter.getOrderEntries());
                    adapter.notifyDataSetChanged();
                    refreshSendLayout();

                }));
    }

    private void orderSent() {
        new AlertDialog.Builder(getActivity())
                .setMessage(getResources().getString(R.string.order_sent))
                .setPositiveButton(getActivity().getResources().getString(R.string.action_ok), null)
                .setCancelable(false)
                .create()
                .show();
    }

    @OnClick(R.id.delete)
    public void clearOrder(){
        new AlertDialog.Builder(getActivity())
                .setMessage(getResources().getString(R.string.clean_order))
                .setPositiveButton(getActivity().getResources().getString(R.string.action_ok), (dialogInterface, i) -> {
                    presenter.cleanCurrentOrder();
                    adapter.updateProductsQuantities(presenter.getOrderEntries());
                    adapter.notifyDataSetChanged();
                    refreshSendLayout();
                })
                .setCancelable(true)
                .create()
                .show();
    }
}
