package com.ysdc.coffee.ui.orders;

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
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.UserOrder;
import com.ysdc.coffee.ui.base.BaseFragment;
import com.ysdc.coffee.ui.utils.MenuDisplayer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

public class OrdersFragment extends BaseFragment implements OrdersMvpView, MenuDisplayer {

    private Integer menuId;
    private CompositeDisposable compositeDisposable;
    private CustomerAdapter adapter;

    @Inject
    OrdersMvpPresenter<OrdersMvpView> presenter;
    @BindView(R.id.orders_list)
    protected RecyclerView ordersList;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress)
    protected ProgressBar progressBar;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        getFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(OrdersFragment.this);
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

        ordersList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ordersList.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadOrders();
        });

        loadOrders();
    }

    private void loadOrders() {
        compositeDisposable.add(presenter.getOrders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    hideProgress();
                    swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(orders -> {
                    if (!orders.isEmpty()) {
                        showOrders(orders);
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

    private void showOrders(List<UserOrder> orders) {
        if (adapter == null) {
            adapter = new CustomerAdapter(orders, this::showDialog, getActivity());
            ordersList.setAdapter(adapter);
        } else {
            adapter.updateOrders(orders);
        }
        adapter.notifyDataSetChanged();
    }

    private void showDialog(UserOrder order) {
                new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.customer_order_status))
                    .setItems(presenter.getStatusString(), (dialogInterface, index) -> {
                        showProgress();
                        presenter.changeOrderStatus(order, index)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> updateContent(), throwable -> onError(throwable));
                    })
                    .setCancelable(true)
                    .create()
                    .show();
    }

    @Override
    public void showMenu(Integer menuId) {
        this.menuId = menuId;
        if (getBaseActivity() != null) {
            getBaseActivity().supportInvalidateOptionsMenu();
        }
    }

    public void updateContent(){
        loadOrders();
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
