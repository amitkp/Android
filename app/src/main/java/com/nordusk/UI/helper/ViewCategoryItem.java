package com.nordusk.UI.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nordusk.R;
import com.nordusk.UI.orderCreate.ArrayAdapterCategory;
import com.nordusk.UI.orderCreate.ArrayAdapterProduct;
import com.nordusk.UI.orderCreate.ProductsAdapter;
import com.nordusk.pojo.DataProduct;
import com.nordusk.pojo.DataProducts;
import com.nordusk.webservices.HttpConnectionUrl;
import com.nordusk.webservices.rest.ProductListAsync;

import java.util.ArrayList;

/**
 * Created by Amit on 19-12-2016.
 */
public class ViewCategoryItem extends FrameLayout {

    private int pos;
    private EditText et_productQty, et_productPrice;
    private Spinner spinner_category,spinner_product;

    ArrayList<String> listcategory = new ArrayList<String>();
    private ArrayList<DataProduct> listProducts=new ArrayList<DataProduct>();
    private  Context context;

    public ViewCategoryItem(Context context, ArrayList<String> listcategory ,ArrayList<DataProduct> listProducts) {
        super(context);
        this.context=context;
        this.listcategory = listcategory;
        this.listProducts=listProducts;
        initView();
    }

    public ViewCategoryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewCategoryItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewCategoryItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_product_item_create, this, true);

        spinner_category = (Spinner) findViewById(R.id.spinner);
        spinner_product = (Spinner) findViewById(R.id.spinner_product);

        et_productQty = (EditText) findViewById(R.id.et_productQty);
        et_productPrice = (EditText) findViewById(R.id.et_productPrice);

        spinner_category.setAdapter(new ArrayAdapterCategory(getContext(), R.layout.layout_spinner, listcategory));

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context, listcategory.get(position), Toast.LENGTH_SHORT).show();
                if (HttpConnectionUrl.isNetworkAvailable(context))
                    if (listcategory.get(position) != null) {
                        String cat = "";
                        cat = listcategory.get(position).toString().trim().replaceAll(" ", "%20");
//                        Toast.makeText(context, cat, Toast.LENGTH_SHORT).show();
                        fetchProduct(cat);
                    } else
                        Toast.makeText(context, "Please check your network connection", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchProduct(String s) {

        ProductListAsync productListAsync = new ProductListAsync(context, s);
        productListAsync.setOnContentListParserListner(new ProductListAsync.OnContentListSchedules() {
            @Override
            public void OnSuccess(ArrayList<DataProduct> arrayList) {
                listProducts = arrayList;
                spinner_product.setAdapter(new ProductsAdapter(context, R.layout.layout_spinner, arrayList));
//                childViewSet(category_list, arrayList);

            }

            @Override
            public void OnError(String str_err) {

            }

            @Override
            public void OnConnectTimeout() {

            }
        });
        productListAsync.execute();

    }

    public void setPosition(int pos) {
        this.pos = pos;
    }

    public String getProductName(){
        return listcategory.get(spinner_category.getSelectedItemPosition()).toLowerCase();
    }

    public String getProductPrice(){
        if (TextUtils.isEmpty(et_productPrice.getText().toString())) {
            return "0";
        }else{
            return et_productPrice.getText().toString();
        }
    }

    public String getProductQuantity(){
//        return mListProducts.get(spinner.getSelectedItemPosition()).getId();
        if (TextUtils.isEmpty(et_productQty.getText().toString())) {
            return "0";
        }else{
            return et_productQty.getText().toString();
        }
    }

    public String getProductId(){
        return listProducts.get(spinner_product.getSelectedItemPosition()).getId();
    }
}
