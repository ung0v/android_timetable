package com.ulan.timetable.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ulan.timetable.R;
import com.ulan.timetable.activities.MainActivity;
import com.ulan.timetable.model.Fee;
import com.ulan.timetable.model.Mark;
import com.ulan.timetable.model.Week;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class ServicesHelper {
    Context mContext;
    List<Week> weekList;
    List<Mark> markList;
    SharedPreferences sharedPreferences;

    public ServicesHelper(Context mContext) {
        this.mContext = mContext;
    }

    public boolean checkDataBase(String path) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    public void getMarkByUrl(String username, String password, final Activity activity) {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://fa98b7d3884e.ngrok.io/getMark";
        final JSONObject jsonBody = new JSONObject();
        Log.d("lads","hh");

        try {
            jsonBody.put("username", username);
            jsonBody.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("login", "okk");


                        try {
                            JSONArray data = response.getJSONArray("data");
//                            Log.d("hoc phi:", String.valueOf(data.getJSONArray(1)));
                            for (int i = 1; i < data.length(); i++) {
                                if (data.getJSONArray(i).length() == 17) {

                                    Mark mark = new Mark();
                                    mark.setS_name((String) data.getJSONArray(i).get(2));
                                    mark.setDiligence((String) data.getJSONArray(i).get(10));
                                    mark.setCredits((String) data.getJSONArray(i).get(3));
                                    mark.setMid_term((String) data.getJSONArray(i).get(11));
                                    mark.setEnd_term((String) data.getJSONArray(i).get(13));
                                    mark.setGrade((String) data.getJSONArray(i).get(16));

                                    DbHelper dbHelper = new DbHelper(mContext);
                                    dbHelper.insertMark(mark);
//                                    Log.d("diem", mark.toString());

                                }

                            }

                            //getfee
                            getFee(jsonBody, activity);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Login", "tên tk hoặc mk không chính xác!");
                Log.d("Login", error.toString());
                activity.findViewById(R.id.txtError).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
        }) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(jsonObjReq);

    }

    public void getFee(final JSONObject jsonObject, final Activity activity) {

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://fa98b7d3884e.ngrok.io/getfee";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("FEE", response.getString("total") + " / " + response.getString("paid") + " / " + response.getString("overage"));
                            Fee fee = new Fee();
                            fee.setTotal(response.getString("total"));
                            fee.setPaid(response.getString("paid"));
                            fee.setOverage(response.getString("overage"));
                            DbHelper dbHelper = new DbHelper(mContext);
                            dbHelper.insertFee(fee);

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        //call back

                        getUserProfile(jsonObject, activity);

//
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fee", "lỗi get fee!");
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);

    }


    public void getWeekByUrl(String url, final Activity activity) {

        RequestQueue queue = Volley.newRequestQueue(mContext);

        weekList = new ArrayList<>();

        JsonArrayRequest getRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response on Success
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.getJSONObject(i);
                                String s_name = jsonObject.getString("lop_hoc_phan"); //lay ten mon hoc
                                String s_location; // dia diem

                                try {
                                    JSONArray location = jsonObject.getJSONArray("dia_diem"); //dia diem neu la mang
                                    s_location = location.getString(1); //lay phan tu thu 2
                                } catch (Exception e) {
                                    s_location = jsonObject.getString("dia_diem"); // != mang => lay string
                                }

                                String s_teacher = jsonObject.getString("giang_vien"); // lay ten giang vien
                                JSONArray s_time = jsonObject.getJSONArray("ranges").getJSONObject(0).getJSONArray("phases"); // lay ve thoi gian (thu+tiet)
                                for (int j = 0; j < s_time.length(); j++) {
                                    JSONObject timeObj = s_time.getJSONObject(j);
                                    String s_fragment = timeObj.getString("day");// lay ve thu
                                    JSONArray from_to = timeObj.getJSONArray("periods"); // lay ve mang tieet

//                                    Log.d("ten mon hoc", s_name);
//                                    Log.d("giang vien", s_teacher);
//                                    Log.d("dia diem", s_location);
//                                    Log.d("res ", convertWeekKey(parseInt(s_fragment)));
//                                    Log.d("fromto ", String.valueOf(CoventTime(from_to)));
//                                    //

                                    DbHelper dbHelper = new DbHelper(mContext);
                                    Week week = new Week();
//                                    Matcher fragment = Pattern.compile("(.*Fragment)").matcher(adapter.getItem(viewPager.getCurrentItem()).toString());
//                                    ColorDrawable buttonColor = (ColorDrawable) select_color.getBackground();
                                    week.setSubject(s_name);
//                                    week.setUserId(1);
                                    week.setFragment(convertWeekKey(parseInt(s_fragment)));
                                    week.setTeacher(s_teacher);
                                    week.setRoom(s_location);
                                    week.setColor(Color.GREEN);

                                    week.setFromTime(CoventTime(from_to).from);
                                    week.setToTime(CoventTime(from_to).to);
                                    dbHelper.insertWeek(week);

                                }


                            }



                            //save key
                            sharedPreferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("connect", true);
                            editor.commit();

                            //intent
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);


                        } catch (JSONException e) {
                            Toast.makeText(mContext, "lỗi không xác định", Toast.LENGTH_SHORT);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext, "Lỗi request", Toast.LENGTH_SHORT);
                    }
                }
        );

        queue.add(getRequest);


    }


    public void getUserProfile(JSONObject jsonObject, final Activity activity) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = "https://fa98b7d3884e.ngrok.io/getprofile";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String studentName = response.getString("studentName");
                            String studentClass = response.getString("studentClass");
                            Log.d("user", studentName +" / "+studentClass);

                            sharedPreferences = activity.getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("studentName", studentName);
                            editor.putString("studentClass", studentClass);
                            editor.commit();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }

                        getWeekByUrl("https://fa98b7d3884e.ngrok.io/utt", activity);
//
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("fee", "lỗi get profile!");
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(jsonObjReq);
    }

    public class FromTo {
        public String from;
        public String to;

        public FromTo(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "FromTo{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    '}';
        }
    }

    public String convertWeekKey(int weekKeyInt) {
        String res;
        switch (weekKeyInt) {
            case 2:
                res = "Monday";
                break;
            case 3:
                res = "Tuesday";
                break;
            case 4:
                res = "Wednesday";
                break;
            case 5:
                res = "Thursday";
                break;
            case 6:
                res = "Friday";
                break;
            default:
                res = "";
                break;
        }

        return res;
    }

    public FromTo CoventTime(JSONArray shiftArray) throws JSONException {
        List<FromTo> fromToList = new ArrayList<>();
        FromTo res;
        for (int i = 0; i < shiftArray.length(); i++) {
            int check = shiftArray.getInt(i);
            String from;
            String to;
            FromTo ft;
            switch (check) {
                case 1:
                    from = "7:00";
                    to = "7:50";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 2:
                    from = "7:55";
                    to = "8:45";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 3:
                    from = "8:55";
                    to = "9:45";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 4:
                    from = "9:55";
                    to = "10:45";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 5:
                    from = "10:50";
                    to = "11:40";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 6:
                    from = "12:30";
                    to = "13:20";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 7:
                    from = "13:25";
                    to = "14:15";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 8:
                    from = "14:25";
                    to = "15:15";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 9:
                    from = "15:25";
                    to = "16:15";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 10:
                    from = "16:20";
                    to = "17:10";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 11:
                    from = "17:15";
                    to = "18:05";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 12:
                    from = "18:15";
                    to = "19:05";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 13:
                    from = "19:10";
                    to = "20:00";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
                case 14:
                    from = "20:05";
                    to = "20:55";
                    ft = new FromTo(from, to);
                    fromToList.add(ft);
                    break;
            }
        }
        res = new FromTo(fromToList.get(0).from, fromToList.get(fromToList.size() - 1).to);

        return res;
    }


}
