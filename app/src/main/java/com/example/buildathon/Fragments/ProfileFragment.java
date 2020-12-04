package com.example.buildathon.Fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.example.buildathon.Adapters.EducationAdapter;
import com.example.buildathon.Adapters.ExperienceAdapter;
import com.example.buildathon.Adapters.ProjectAdapter;
import com.example.buildathon.Adapters.SkillAdapter;
import com.example.buildathon.MainActivity;
import com.example.buildathon.Model.AccomplishmentModel;
import com.example.buildathon.Model.EducationModel;
import com.example.buildathon.Model.ExperienceModel;
import com.example.buildathon.Model.SkillModel;
import com.example.buildathon.Model.UserModel;
import com.example.buildathon.R;
import com.example.buildathon.Utils.LoadingBar;
import com.example.buildathon.Utils.SessionManagment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.buildathon.Utils.Constants.KEY_DESCRIPTION;
import static com.example.buildathon.Utils.Constants.KEY_LOCATION;
import static com.example.buildathon.Utils.Constants.KEY_MOBILE;
import static com.example.buildathon.Utils.Constants.KEY_NAME;
import static com.example.buildathon.Utils.Constants.KEY_PROFILE_IMG;
import static com.example.buildathon.Utils.Constants.KEY_TITLE;
import static com.example.buildathon.Utils.Constants.USER_REF;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    private static final int PICK_IMAGE_REQUEST =1;
    SessionManagment sessionManagment;
    String name , title , description , location;
    TextView txtname , txttitle  , txtloc;
    ReadMoreTextView txtdesc;
    ImageView editAbout,edit_education,edit_accomplish,edit_experience,edit_skill,edit_img;
    List<EducationModel> education;
    LoadingBar loadingBar;
    List<AccomplishmentModel> project;
    List<ExperienceModel> experience;
    List<SkillModel> skill;
    ExperienceAdapter experienceAdapter;
    DatabaseReference userref;
    UserModel model;
    RecyclerView recyclerEducation,recyclerProject,recyclerExperience,recyclerSkill;
    EducationAdapter educationAdapter;
    SkillAdapter skillAdapter;
    ProjectAdapter projectAdapter;
    TextView txtexp , txtEdu , txtPro,txtSkill;
    private Uri mImageUri;
    StorageReference mStorageRef;
    CircleImageView profileimg;
    private StorageTask mUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity)getActivity()).setTitle("Profile");
        sessionManagment = new SessionManagment(getActivity());
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        loadingBar = new LoadingBar(getActivity());
        loadingBar.show();
        userref = FirebaseDatabase.getInstance().getReference().child(USER_REF).child(sessionManagment.getUserDetails().get(KEY_MOBILE));
        txtname = v.findViewById(R.id.txt_name);
        txttitle = v.findViewById(R.id.txt_title);
        txtdesc = (ReadMoreTextView)v.findViewById(R.id.txt_about);
        edit_education = v.findViewById(R.id.edit_education);
        edit_accomplish = v.findViewById(R.id.edit_accomplish);
        edit_skill = v.findViewById(R.id.edit_skills);
        profileimg = v.findViewById(R.id.profile_img);
        edit_experience = v.findViewById(R.id.edit_experience);
        recyclerEducation = v.findViewById(R.id.recyclerEducation);
        recyclerProject = v.findViewById(R.id.recyclerAccomplish);
        recyclerSkill = v.findViewById(R.id.recyclerSkill);
        txtEdu = v.findViewById(R.id.txtEdu);
        txtexp = v.findViewById(R.id.txtExpe);
        txtPro = v.findViewById(R.id.txtPro);
        txtSkill = v.findViewById(R.id.txtSkill);
        recyclerExperience = v.findViewById(R.id.recyclerExperience);
        txtloc = v.findViewById(R.id.txt_location);
        edit_img = v.findViewById(R.id.edit_img);
        editAbout = v.findViewById(R.id.edit_about);
        education = new ArrayList<>();
        skill = new ArrayList<>();
        project = new ArrayList<>();
        experience = new ArrayList<>();
        updateAbout();
        getData();
        //educationModelList.add(new EducationModel("BIET JHANSI","BTech","CSE","08-07-2018","30-05-2022","A","Mst Mzaa Aaya"));
        editAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).loadFragment(new AboutFragment(),null);
            }
        });
        edit_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type","a");
                bundle.putSerializable("model",model.getEducation());
                ((MainActivity)getActivity()).loadFragment(new EducationFragment(),bundle);
            }
        });
        edit_accomplish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type","a");
                bundle.putSerializable("model",model.getProject());
                ((MainActivity)getActivity()).loadFragment(new ProjectFragment(),bundle);
            }
        });
        edit_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type","a");
                bundle.putSerializable("model",model.getExperience());
                ((MainActivity)getActivity()).loadFragment(new ExperienceFragment(),bundle);
            }
        });
        edit_skill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type","a");
                bundle.putSerializable("model",model.getSkill());
                ((MainActivity)getActivity()).loadFragment(new SkillFragment(),bundle);
            }
        });
        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        return  v;

    }

    private void getEducation() {
        List<EducationModel> educationList = model.getEducation();
        recyclerEducation.setLayoutManager(new GridLayoutManager(getActivity(),1));
        educationAdapter= new EducationAdapter(educationList,getActivity());
        recyclerEducation.setAdapter(educationAdapter);
        educationAdapter.notifyDataSetChanged();

    }
    private void getProjects() {
        List<AccomplishmentModel> projectList = model.getProject();
        recyclerProject.setLayoutManager(new GridLayoutManager(getActivity(),1));
        projectAdapter= new ProjectAdapter(projectList,getActivity());
        recyclerProject.setAdapter(projectAdapter);
        projectAdapter.notifyDataSetChanged();

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR=getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void getData() {
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("DATA",snapshot.toString());
                model=snapshot.getValue(UserModel.class);
                education = model.getEducation();
                project = model.getProject();
                experience = model.getExperience();
                skill = model.getSkill();
                Log.e("yess",model.toString());
                if (education!=null)
                {
                    getEducation();
                }
                else
                {
                    txtEdu.setText("Add Educational Details");
                    txtEdu.setTextColor(getResources().getColor(R.color.purple_700));
                }
                if (project!=null)
                {
                    getProjects();
                }
                else
                {
                    txtPro.setText("Add Projects");
                    txtPro.setTextColor(getResources().getColor(R.color.purple_700));
                }
                if (experience!=null)
                {
                    getExperience();
                }
                else
                {
                    txtexp.setText("Add Work Experience");
                    txtexp.setTextColor(getResources().getColor(R.color.purple_700));
                }
                if (skill!=null)
                {
                    getSkill();
                }
                else
                {
                    txtSkill.setText("Add Skills");
                    txtSkill.setTextColor(getResources().getColor(R.color.purple_700));
                }
                Glide.with(getActivity()).load(sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)).into(profileimg);
                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingBar.dismiss();
            }
        });
    }

    private void getSkill(){
        List<SkillModel> skillList = model.getSkill();
        recyclerSkill.setLayoutManager(new GridLayoutManager(getActivity(),1));
        skillAdapter= new SkillAdapter(skillList,getActivity());
        recyclerSkill.setAdapter(skillAdapter);
        skillAdapter.notifyDataSetChanged();
    }

    private void getExperience() {
        List<ExperienceModel> experienceList = model.getExperience();
        recyclerExperience.setLayoutManager(new GridLayoutManager(getActivity(),1));
        experienceAdapter= new ExperienceAdapter(experienceList,getActivity());
        recyclerExperience.setAdapter(experienceAdapter);
        experienceAdapter.notifyDataSetChanged();
    }

    private void updateAbout() {
        title = sessionManagment.getUserDetails().get(KEY_TITLE);
        description = sessionManagment.getUserDetails().get(KEY_DESCRIPTION);
        location = sessionManagment.getUserDetails().get(KEY_LOCATION);
        if (title==null||title.equalsIgnoreCase(""))
            txttitle.setVisibility(View.GONE);
        if (description==null||description.equalsIgnoreCase(""))
            txtdesc.setVisibility(View.GONE);
        if (location==null||location.equalsIgnoreCase(""))
            txtloc.setVisibility(View.GONE);
        txtname.setText(sessionManagment.getUserDetails().get(KEY_NAME));
        txttitle.setText(title);
        txtloc.setText(location);
        txtdesc.setText(description);
    }
    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                && data!= null && data.getData()!=null){
            mImageUri=data.getData();
            uploadFile();
        }
    }
    private void uploadFile(){
        loadingBar.show();
        if(mImageUri!=null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));
            mUploadTask=fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(),"Uplaod Successfull",Toast.LENGTH_SHORT).show();
                            Task<Uri> urlTask= taskSnapshot.getStorage().getDownloadUrl();
                            while(!urlTask.isSuccessful());
                            Uri downloadUrl=urlTask.getResult();
                            model.setImg_url(downloadUrl.toString());
                            userref.setValue(model);
                            sessionManagment.updateProfile(model.getName(),model.getEmail(),downloadUrl.toString(),model.getStatus(),model.getTitle(),model.getLocation(),model.getDescription());
                            loadingBar.dismiss();
                            Glide.with(getActivity()).load(sessionManagment.getUserDetails().get(KEY_PROFILE_IMG)).into(profileimg);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity() , e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){

                        }
                    });
        }
        else{
            Toast.makeText(getActivity(),"No File Selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    if(mUploadTask!= null&& mUploadTask.isInProgress()){
                        Toast.makeText(getActivity(),"Upload in Progress",Toast.LENGTH_LONG).show();
                    }
                    else
                        ((MainActivity)getActivity()).onBackPressed();
                    return true;
                }
                return false;
            }
        });
    }
}