package com.acmefyblue.statussaver.ui;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acmefyblue.statussaver.R;
import com.acmefyblue.statussaver.adapter.SavedAdapter;
import com.acmefyblue.statussaver.model.Status;
import com.acmefyblue.statussaver.utils.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SavedFilesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private final List<Status> savedFilesList = new ArrayList<>();
    private final Handler handler = new Handler();
    private SavedAdapter savedAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView no_files_found;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewFiles);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutFiles);
        progressBar = view.findViewById(R.id.progressBar);
        no_files_found = view.findViewById(R.id.no_files_found);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireActivity(), R.color.green1)
                , ContextCompat.getColor(requireActivity(), R.color.green1),
                ContextCompat.getColor(requireActivity(), R.color.green1),
                ContextCompat.getColor(requireActivity(), R.color.green1));

        swipeRefreshLayout.setOnRefreshListener(this::getFiles);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Common.GRID_COUNT));

        getFiles();

    }


    private void getFiles() {

        final File app_dir = new File(Environment.getExternalStorageDirectory() +
                File.separator + Common.APP_DIR);

        if (app_dir.exists()) {


            no_files_found.setVisibility(View.GONE);

            new Thread(() -> {
                File[] savedFiles;
                savedFiles = app_dir.listFiles();
                savedFilesList.clear();

                if (savedFiles != null && savedFiles.length > 0) {

                    Arrays.sort(savedFiles);

                    for (File file : savedFiles) {
                        Status status = new Status(file, file.getName(), file.getAbsolutePath());

                        savedFilesList.add(status);
                    }

                    handler.post(() -> {

                        savedAdapter = new SavedAdapter(savedFilesList);
                        recyclerView.setAdapter(savedAdapter);
                        savedAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    });

                } else {

                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        no_files_found.setVisibility(View.VISIBLE);
                    });

                }
                swipeRefreshLayout.setRefreshing(false);
            }).start();

        } else {
            Toast.makeText(getActivity(), "Dir doest not exists", Toast.LENGTH_SHORT).show();

            no_files_found.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

    }
}
