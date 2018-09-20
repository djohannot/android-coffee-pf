package com.ysdc.coffee.ui.create;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ysdc.coffee.R;
import com.ysdc.coffee.data.model.CupSize;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.injection.module.GlideApp;
import com.ysdc.coffee.ui.base.BaseBottomSheetFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import io.reactivex.disposables.CompositeDisposable;

public class CreateOrderFragment extends BaseBottomSheetFragment implements CreateOrderMvpView {

    private static final String EXTRA_ORDER_PRODUCT = "EXTRA_ORDER_PRODUCT";

    @Inject
    CreateOrderMvpPresenter<CreateOrderMvpView> presenter;

    @BindView(R.id.product_image)
    protected ImageButton productImage;
    @BindView(R.id.product_name)
    protected TextView productName;
    @BindView(R.id.product_quantity)
    protected TextView productQuantity;
    @BindView(R.id.size_small)
    protected ImageButton smallBtn;
    @BindView(R.id.size_medium)
    protected ImageButton mediumBtn;
    @BindView(R.id.size_large)
    protected ImageButton largeBtn;
    @BindView(R.id.sugar_0)
    protected ImageButton sugar0;
    @BindView(R.id.sugar_1)
    protected ImageButton sugar1;
    @BindView(R.id.sugar_2)
    protected ImageButton sugar2;
    @BindView(R.id.sugar_3)
    protected ImageButton sugar3;

    private CompositeDisposable subscriptions;
    private CreateListener listener;

    public interface CreateListener {
        void onAddOrderPressed();
    }

    public static CreateOrderFragment newInstance(CreateListener listener, OrderedProduct orderedProduct) {
        CreateOrderFragment createOrderFragment = new CreateOrderFragment();
        createOrderFragment.listener = listener;
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ORDER_PRODUCT, orderedProduct);
        createOrderFragment.setArguments(bundle);
        return createOrderFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_customize, container, false);
        getFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        Bundle arguments = getArguments();
        if (arguments != null) {
            presenter.setOrderProduct(arguments.getParcelable(EXTRA_ORDER_PRODUCT));
        }
        presenter.onAttach(CreateOrderFragment.this);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setSkipCollapsed(true);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return dialog;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.subscriptions = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        subscriptions.dispose();
        super.onStop();
    }

    @Override
    protected void setUp(View view) {
        if (!TextUtils.isEmpty(presenter.getProduct().getImageUrl())) {
            GlideApp.with(getActivity())
                    .load(presenter.getProduct().getImageUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(productImage);
        }
        productName.setText(presenter.getProduct().getName());

    }

    @OnClick(R.id.back_button)
    public void backPressed() {
        dismiss();
    }

    @OnClick(R.id.quantity_add)
    public void addPressed() {
        presenter.incrementQuantity();
    }

    @OnClick(R.id.quantity_substract)
    public void substractPressed() {
        presenter.decrementQuantity();
    }

    @Override
    public void updateQuantity(int quantity) {
        productQuantity.setText(String.valueOf(quantity));
    }

    @OnClick(R.id.size_large)
    public void largePressed() {
        largeBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.large_selected));
        mediumBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.medium_normal));
        smallBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.small_normal));
        presenter.setSizeSelected(CupSize.LARGE);
    }

    @OnClick(R.id.size_medium)
    public void mediumPressed() {
        largeBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.large_normal));
        mediumBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.medium_selected));
        smallBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.small_normal));
        presenter.setSizeSelected(CupSize.MEDIUM);
    }

    @OnClick(R.id.size_small)
    public void smallPressed() {
        largeBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.large_normal));
        mediumBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.medium_normal));
        smallBtn.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.small_selected));
        presenter.setSizeSelected(CupSize.SMALL);
    }

    @OnClick(R.id.sugar_0)
    public void noSugarPressed() {
        sugar0.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_0_selected));
        sugar1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_1));
        sugar2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_2));
        sugar3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_3));
        presenter.setSugarSelected(0);
    }

    @OnClick(R.id.sugar_1)
    public void sugar1Pressed() {
        sugar0.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_0));
        sugar1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_1_selected));
        sugar2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_2));
        sugar3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_3));
        presenter.setSugarSelected(1);
    }

    @OnClick(R.id.sugar_2)
    public void sugar2Pressed() {
        sugar0.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_0));
        sugar1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_1));
        sugar2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_2_selected));
        sugar3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_3));
        presenter.setSugarSelected(2);
    }

    @OnClick(R.id.sugar_3)
    public void sugar3Pressed() {
        sugar0.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_0));
        sugar1.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_1));
        sugar2.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_2));
        sugar3.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.sugar_3_selected));
        presenter.setSugarSelected(3);
    }

    @OnTouch(R.id.carmel_flavor)
    public boolean carmelChanged(Switch switchView, MotionEvent event) {
        presenter.carmelSelected(event.getAction() == MotionEvent.ACTION_UP);
        return true;
    }

    @OnTouch(R.id.toffeenut_flavor)
    public boolean toffeenutChanged(Switch switchView, MotionEvent event) {
        presenter.toffeenutSelected(event.getAction() == MotionEvent.ACTION_UP);
        return true;
    }

    @OnTouch(R.id.vanilla_flavor)
    public boolean vanillaChanged(Switch switchView, MotionEvent event) {
        presenter.vanillaSelected(event.getAction() == MotionEvent.ACTION_UP);
        return true;
    }

    @OnTouch(R.id.take_away)
    public boolean takeAwayChanged(Switch switchView, MotionEvent event) {
        presenter.isTakeAway(event.getAction() == MotionEvent.ACTION_UP);
        return true;
    }

    @OnClick(R.id.btn_order)
    public void addOrderPressed() {
        subscriptions.add(
                presenter.addOrder().subscribe(() -> {
                    listener.onAddOrderPressed();
                    dismiss();
                })
        );
    }
}
