package com.example.qrgo.utilities;


import androidx.core.content.FileProvider;

import com.example.qrgo.R;

public class LocationPhotoFileProvider extends FileProvider {
    public LocationPhotoFileProvider() {
        super(R.xml.file_paths);
    }
}
