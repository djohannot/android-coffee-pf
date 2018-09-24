package com.ysdc.coffee.ui.history;

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
import com.ysdc.coffee.data.model.Order;
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

public class HistoryFragment extends BaseFragment implements HistoryMvpView, MenuDisplayer {

    private Integer menuId;
    private CompositeDisposable compositeDisposable;
    private OrderAdapter adapter;

    @Inject
    HistoryMvpPresenter<HistoryMvpView> presenter;
    @BindView(R.id.order_list)
    protected RecyclerView ordersList;
    @BindView(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        getFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(HistoryFragment.this);
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
        compositeDisposable.add(presenter.getOrderedProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    swipeRefreshLayout.setRefreshing(false);
                })
                .subscribe(orderedProducts -> {
                    if (!orderedProducts.isEmpty()) {
                        showOrders(orderedProducts);
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

    private void showOrders(List<Order> products) {
        if (adapter == null) {
            adapter = new OrderAdapter(products, this::showDialog, getActivity());
            ordersList.setAdapter(adapter);
        } else {
            adapter.updateOrders(products);
        }
        adapter.notifyDataSetChanged();
    }

    private void showDialog(Order order) {
        presenter.mergeWithCurrentOrder(order);
    }

    @Override
    public void showMenu(Integer menuId) {
        this.menuId = menuId;
        if (getBaseActivity() != null) {
            getBaseActivity().supportInvalidateOptionsMenu();
        }
    }

    public void updateContent() {
        loadOrders();
    }
}
