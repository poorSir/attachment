package com.example.szh.upload;

import android.util.Log;

public class AttachmentModel {
    private String file1;
    private String file2;
    private String file3;
    private String file4;
    private String file5;

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public String getFile2() {
        return file2;
    }

    public void setFile2(String file2) {
        this.file2 = file2;
    }

    public String getFile3() {
        return file3;
    }

    public void setFile3(String file3) {
        this.file3 = file3;
    }

    public String getFile4() {
        return file4;
    }

    public void setFile4(String file4) {
        this.file4 = file4;
    }

    public String getFile5() {
        return file5;
    }

    public void setFile5(String file5) {
        this.file5 = file5;
    }
    public void setFile(int num,String file){
        switch (num){
            case 1:
                setFile1(file);
                break;
            case 2:
                setFile2(file);
                break;
            case 3:
                setFile3(file);
                break;
            case 4:
                setFile4(file);
                break;
            case 5:
                setFile5(file);
                break;
        }
    }
    public String getFile(int num){
        switch (num){
            case 1:
                return file1;
            case 2:
                return file2;
            case 3:
                return file3;
            case 4:
                return file4;
            case 5:
                return file5;
        }
        return null;
    }

}
