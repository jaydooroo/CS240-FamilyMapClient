package UI;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mainactivity.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Data.DataCache;
import Server.GetDataTask;
import Server.LoginTask;
import Server.RegisterTask;
import model.Person;

public class LoginFragment extends Fragment  {

    private EditText editUserName;
    private EditText editServerHost;
    private EditText editServerPort;
    private EditText editPassword;
    private EditText editEmail;
    private EditText editFirstName;
    private EditText editLastName;
    private RadioGroup editRadioGender;


    private String gender;
    private Button loginButton;
    private Button registerButton;
    private Listener listener;

    // To keep the key for message which comes from other threads
    private String SUCCESS_KEY = "SUCCESS_KEY";

    public interface Listener {
        void notifyLogin();
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DataCache dataCache = DataCache.getInstance();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        loginButton = view.findViewById(R.id.LoginButton);
        registerButton = view.findViewById(R.id.RegisterButton);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        //Login Requirements
        editUserName = view.findViewById(R.id.UserName);
        editServerHost = view.findViewById(R.id.ServerHost);
        editPassword = view.findViewById(R.id.Password);
        editServerPort = view.findViewById(R.id.ServerPort);

        //Additional Registration requirement
        editFirstName = view.findViewById(R.id.FirstName);
        editLastName = view.findViewById(R.id.LastName);
        editEmail = view.findViewById(R.id.Email);
        editRadioGender = view.findViewById(R.id.genderRadioGroup);

        //Login Requirements
        textListener(editUserName);
        textListener(editPassword);
        textListener(editServerHost);
        textListener(editServerPort);

        //Additional Registration requirement
        textListener(editFirstName);
        textListener(editLastName);
        textListener(editEmail);

        editRadioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int genderID = radioGroup.getCheckedRadioButtonId();
                switch (genderID) {
                    case R.id.Male:
                        gender = "m";
                        break;
                    case  R.id.Female:
                        gender = "f";
                        break;
                }

                checkButtonOpen();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /* if(){

                }else */ if (listener != null) {
                    loginTask(editServerHost.getText().toString(),editServerPort.getText().toString(),
                            editUserName.getText().toString(),editPassword.getText().toString());
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    registerTask(editServerHost.getText().toString(),editServerPort.getText().toString(),editUserName.getText().toString(),
                            editPassword.getText().toString(),editEmail.getText().toString(),editFirstName.getText().toString(),editLastName.getText().toString(),
                            gender);
                }
            }
        });



        return view;

    }
    private void textListener(EditText text) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkButtonOpen();
            }
        });
    }

    private void checkButtonOpen() {
        if(!editUserName.getText().toString().equals("") && !editServerHost.getText().toString().equals("")
                && !editServerPort.getText().toString().equals("") && !editPassword.getText().toString().equals("")) {

            loginButton.setEnabled(true);
            if(!editFirstName.getText().toString().equals("") && !editLastName.getText().toString().equals("")
                    && !editEmail.getText().toString().equals("") && gender != null) {
                registerButton.setEnabled(true);
            }
            else {
                registerButton.setEnabled(false);
            }
        }
        else {
            loginButton.setEnabled(false);
        }

    }

    public void registerListener(Listener listener) {

        this.listener = listener;
    }

    public void registerTask (String serverHost, String serverPort, String username, String password,
                              String email, String firstName, String lastName, String gender) {


        DataCache dataCache = DataCache.getInstance();

        dataCache.clear();
        @SuppressLint("HandlerLeak") Handler loginUiThreadHandler = new Handler() {

            @Override
            public void handleMessage(Message message) {

                Bundle bundle = message.getData();
                boolean success = bundle.getBoolean(SUCCESS_KEY);

                if(success) {
                    getDataTask(serverHost,serverPort);
                    Toast.makeText(getActivity(),

                            //Not sure if this work, Check it our later
                            "Register Succeed",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Register Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        };


        RegisterTask registerTask = new RegisterTask(loginUiThreadHandler,serverHost,serverPort,
                username,password,email,firstName,lastName,gender);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(registerTask);



    }

    public void loginTask (String serverHost, String serverPort, String username, String password) {


        DataCache dataCache = DataCache.getInstance();

        dataCache.clear();

        // Not really sure if I can make the handler at here.
        @SuppressLint("HandlerLeak") Handler loginUiThreadHandler = new Handler() {

            @Override
            public void handleMessage(Message message) {

                Bundle bundle = message.getData();
                boolean success =bundle.getBoolean(SUCCESS_KEY);


                if(success) {
                    getDataTask(serverHost,serverPort);
                    Toast.makeText(getActivity(),

                            //Not really sure this way works,Ask Ta about it.
                            "Login Succeed",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Login Failed",
                            Toast.LENGTH_LONG).show();
                }
            }
        };


        LoginTask login = new LoginTask(loginUiThreadHandler,serverHost,serverPort,username,password);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(login);
    }


    public void getDataTask( String serverHost, String serverPort) {

        @SuppressLint("HandlerLeak") Handler getDataThreadHandler = new Handler() {

            @Override
            public void handleMessage(Message message) {

                DataCache dataCache = DataCache.getInstance();
                Bundle bundle = message.getData();
                boolean success =bundle.getBoolean(SUCCESS_KEY);


                if(success) {
                      Person user = dataCache.getPeople().get(dataCache.getPersonID());
                      String successMessage = "User Name: "+ user.getFirstName() + " " + user.getLastName();
                    Toast.makeText(getActivity(),
                            //Not really sure this way works,Ask Ta about it.
                            successMessage,
                            Toast.LENGTH_LONG).show();
                    listener.notifyLogin();
                }
                else {
                    Toast.makeText(getActivity(),
                            "Data Task Failed",
                            Toast.LENGTH_LONG).show();
                }

            }
        };

        GetDataTask getDataTask = new GetDataTask(getDataThreadHandler,serverHost,serverPort);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(getDataTask);
    }


}