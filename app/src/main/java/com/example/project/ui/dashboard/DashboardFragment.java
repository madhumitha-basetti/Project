package com.example.project.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project.Customer;
import com.example.project.R;
import com.example.project.databinding.FragmentDashboardBinding;

import androidx.appcompat.widget.AppCompatSpinner;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private FragmentDashboardBinding binding;

    LinearLayout layoutList;
    Button buttonAdd;
    Button buttonSubmitList;
    public RadioGroup rg;
    RadioButton rb;



    List<String> itemList = new ArrayList<>();
    List<String> quanList=new ArrayList<>();
   ArrayList<Customer> customersList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        buttonAdd = (Button) root.findViewById(R.id.button_add);
        buttonSubmitList = (Button) root.findViewById(R.id.button_submit_list);
        layoutList = (LinearLayout) root.findViewById(R.id.layout_list);
        buttonAdd.setOnClickListener(this);
        rg = (RadioGroup) root.findViewById(R.id.radgroup);

        buttonSubmitList.setOnClickListener(this);

        itemList.add(0,"Select Item");
        itemList.add("Sand");
        itemList.add("Bricks");
        itemList.add("Stones");
        itemList.add("Cement");
        quanList.add(0,"Select Quantity");
        quanList.add("1");
        quanList.add("2");
        quanList.add("3");
        quanList.add("4");
        quanList.add("5");
        quanList.add("6");

       // final TextView textView = binding.textDashboard;
      //  dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String d=i2+"-"+(i1+1)+"-"+i;
                binding.text.setText(d);
                removeView();

            }
        });

        return root;


    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.button_add:

              addView();

                break;
            case R.id.button_submit_list:
               if(checkIfValidAndRead()) {
               //    Log.d("TAG","succuss");
                   Toast.makeText(getContext(),"Succuss",Toast.LENGTH_LONG).show();
               }
                break;


        }
    }

    private boolean checkIfValidAndRead() {
        customersList.clear();
        boolean result=true;

        for(int i=0;i<layoutList.getChildCount();i++){
            View customerView =layoutList.getChildAt(i);

            EditText editCustomerName=(EditText)customerView.findViewById(R.id.edit_customer_name);
            EditText editAmount=(EditText)customerView.findViewById(R.id.edit_customer_amount);

            AppCompatSpinner spinnerItem = (AppCompatSpinner)customerView.findViewById(R.id.spinner_item);
            AppCompatSpinner spinnerQuan = (AppCompatSpinner)customerView.findViewById(R.id.spinner_Quantity);
            RadioGroup radg=customerView.findViewById(R.id.radgroup);
            int selectedId=radg.getCheckedRadioButtonId();
            RadioButton radb=(RadioButton)customerView.findViewById(selectedId);
            Customer customer=new Customer();

            if(!editCustomerName.getText().toString().equals("")){
                customer.setCustomerName(editCustomerName.getText().toString());
           //     Toast.makeText(getContext(),customer.getCustomerName(),Toast.LENGTH_SHORT).show();
            }else{
                result=false;
            }
            if(!editAmount.getText().toString().equals("")){
                customer.setAmount(editAmount.getText().toString());
           //     Toast.makeText(getContext(),customer.getAmount(),Toast.LENGTH_SHORT).show();
            }else{
                result=false;
            }
            if(spinnerItem.getSelectedItemPosition()!=0){
                customer.setItemName(itemList.get(spinnerItem.getSelectedItemPosition()));
              //      Toast.makeText(getContext(),customer.getItemName(),Toast.LENGTH_SHORT).show();


            }else{
                result=false;
            }
            if(spinnerQuan.getSelectedItemPosition()!=0){
                customer.setQuantity(quanList.get(spinnerQuan.getSelectedItemPosition()));
                   // Toast.makeText(getContext(),customer.getQuantity(),Toast.LENGTH_SHORT).show();

            }else{
                result=false;
            }

           if(!radb.getText().toString().equals("")){
                if(radb.getText().toString().equals("Paid")){
                    customer.setStatus("Paid");

                }
                else{
                    customer.setStatus("Unpaid");

                }
               if(customer.Status!=null){
                 //  Toast.makeText(getContext(),customer.Status,Toast.LENGTH_SHORT).show();
               }

            }else{
                result=false;
            }
            Toast.makeText(getContext(),customer.getCustomerName()+"-"+customer.getItemName()+"-"+
                    customer.getQuantity()+"-"+customer.getAmount()+"-"+customer.getStatus(),Toast.LENGTH_SHORT).show();
            customersList.add(customer);
        }
            if(customersList.size()==0){
                result=false;
                Toast.makeText(getContext(),"Add Customers First",Toast.LENGTH_SHORT).show();
            }else if(!result){
                Toast.makeText(getContext(),"Enter details Correctly",Toast.LENGTH_SHORT).show();
            }

        return result;
    }


    private void addView() {

        final View customerView = getLayoutInflater().inflate(R.layout.row_add_items,null,false);

        EditText editCustname = (EditText)customerView.findViewById(R.id.edit_customer_name);
        EditText editAmount=(EditText)customerView.findViewById(R.id.edit_customer_amount);
        AppCompatSpinner spinnerTeam = (AppCompatSpinner)customerView.findViewById(R.id.spinner_item);
        ArrayAdapter arrayAdapter = new ArrayAdapter(customerView.getContext(),android.R.layout.simple_spinner_item,itemList);
        spinnerTeam.setAdapter(arrayAdapter);

        AppCompatSpinner spinnerQuan = (AppCompatSpinner)customerView.findViewById(R.id.spinner_Quantity);
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(customerView.getContext(),android.R.layout.simple_spinner_item,quanList);
        spinnerQuan.setAdapter(arrayAdapter2);

        rg=customerView.findViewById(R.id.radgroup);
        layoutList.addView(customerView);

    }


  /*  public void checkButton (View v){
        int radiobtnid=rg.getCheckedRadioButtonId();
        rb=(RadioButton) getActivity().findViewById(radiobtnid);
    }
    */


    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch(i){
                case R.id.radio_paid:
                    break;
                case R.id.radio_unpaid:
                    break;
            }
        }
    };
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void removeView(){
        layoutList.removeAllViews();
    }
}
