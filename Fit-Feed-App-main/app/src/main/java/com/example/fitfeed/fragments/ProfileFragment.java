package com.example.fitfeed.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitfeed.R;
import com.example.fitfeed.activities.FriendsActivity;
import com.example.fitfeed.activities.GymSelectorMapsActivity;
import com.example.fitfeed.adapters.WorkoutsRecyclerViewAdapter;
import com.example.fitfeed.models.Friend;
import com.example.fitfeed.models.Workout;
import com.example.fitfeed.util.APIManager;
import com.example.fitfeed.util.FileManager;
import com.example.fitfeed.util.TokenManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Fragment for displaying the profile view
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button addFriendsButton;
    private ImageButton editHomeGymButton;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listener for add friends
        addFriendsButton = getView().findViewById(R.id.addFriendsButton);
        addFriendsButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FriendsActivity.class);
            startActivity(intent);
        });

        // Listener for edit home gym
        editHomeGymButton = getView().findViewById(R.id.editHomeGymButton);
        editHomeGymButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), GymSelectorMapsActivity.class);
            startActivity(intent);
        });

        // Get the selected home gym from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.fitfeed", Context.MODE_PRIVATE);
        String homeGym = sharedPref.getString("home_gym", getString(R.string.no_home_gym));

        // Set the selected home gym from SharedPreferences
        TextView homeGymValue = getView().findViewById(R.id.homeGymValue);
        homeGymValue.setText(homeGym);

        // Set the username
        String username = TokenManager.getUsername();
        TextView usernameValue = getView().findViewById(R.id.username);
        usernameValue.setText(username);
        updateProfileStats();
    }

    /**
     * Update values when profile view resumes
     */
    @Override
    public void onResume() {
        super.onResume();

        // Get the selected home gym from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("com.example.fitfeed", Context.MODE_PRIVATE);
        String homeGym = sharedPref.getString("home_gym", getString(R.string.no_home_gym));  // Default value if none is set

        // Set the selected home gym from SharedPreferences
        TextView homeGymValue = getView().findViewById(R.id.homeGymValue);
        homeGymValue.setText(homeGym);
    }

    private void updateProfileStats() {
        MutableLiveData<List<Workout>> workoutData = APIManager.GetWorkouts();
        workoutData.observe(getViewLifecycleOwner(), workouts -> {
            if(!workouts.isEmpty())
            {
                int workoutCount = workouts.size();
                TextView workoutCountView = getView().findViewById(R.id.workoutsValue);
                workoutCountView.setText(String.valueOf(workoutCount));
            }
        });
        MutableLiveData<List<Friend>> friendsData = APIManager.getFriends();
        friendsData.observe(getViewLifecycleOwner(), friends -> {
            if(!friends.isEmpty())
            {
                int friendsCount = friends.size();
                TextView followersCountView = getView().findViewById(R.id.followersValue);
                followersCountView.setText(String.valueOf(friendsCount));
                TextView followingCountView = getView().findViewById(R.id.followingValue);
                followingCountView.setText(String.valueOf(friendsCount));
            }
        });
    }
}