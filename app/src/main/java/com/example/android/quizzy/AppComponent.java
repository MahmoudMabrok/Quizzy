package com.example.android.quizzy;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    // void inject(LoginFragment loginFragment);

    //  void inject(RegisterStudentFragment registerStudentFragment);

//    void inject(RegisterTeacherFragment registerTeacherFragment);

}
