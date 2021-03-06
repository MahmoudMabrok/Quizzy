package com.example.android.quizzy.viewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface LoginViewModel {

    Completable register(HashMap<String, Object> body);

    Maybe<FirebaseUser> login(HashMap<String, String> body);

}
