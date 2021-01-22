package com.mobatia.bisad.rest

import com.mobatia.bisad.activity.absence.model.RequestAbsenceApiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailApiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListAPiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel
import com.mobatia.bisad.activity.home.model.DataCollectionSubmissionModel
import com.mobatia.bisad.activity.message.model.MessageDetailApiModel
import com.mobatia.bisad.activity.message.model.MessageDetailModel
import com.mobatia.bisad.activity.settings.termsofservice.model.TermsOfServiceModel
import com.mobatia.bisad.activity.term_dates.model.TermDatesDetailApiModel
import com.mobatia.bisad.activity.term_dates.model.TermDatesDetailModel
import com.mobatia.bisad.fragment.apps.model.AppsApiModel
import com.mobatia.bisad.fragment.apps.model.AppsListModel
import com.mobatia.bisad.fragment.attendance.model.AttendanceApiModel
import com.mobatia.bisad.fragment.attendance.model.AttendanceListModel
import com.mobatia.bisad.fragment.calendar.model.CalendarApiModel
import com.mobatia.bisad.fragment.calendar.model.CalendarListModel
import com.mobatia.bisad.fragment.calendar.model.CalendarModel
import com.mobatia.bisad.fragment.contact_us.model.ContactusModel
import com.mobatia.bisad.fragment.curriculum.model.CuriculumListModel
import com.mobatia.bisad.fragment.curriculum.model.CurriculumStudentApiModel
import com.mobatia.bisad.fragment.home.model.BannerModel
import com.mobatia.bisad.fragment.home.model.CountryListModel
import com.mobatia.bisad.fragment.home.model.RelationShipListModel
import com.mobatia.bisad.fragment.home.model.TilesListModel
import com.mobatia.bisad.fragment.home.model.datacollection.DataCollectionModel
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceListModel
import com.mobatia.bisad.fragment.reports.model.ReportApiModel
import com.mobatia.bisad.fragment.reports.model.ReportListModel
import com.mobatia.bisad.fragment.settings.model.ChangePasswordApiModel
import com.mobatia.bisad.fragment.settings.model.TriggerUSer
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaListModel
import com.mobatia.bisad.fragment.student_information.model.StudentInfoApiModel
import com.mobatia.bisad.fragment.student_information.model.StudentInfoModel
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.teacher_contact.model.SendStaffMailApiModel
import com.mobatia.bisad.fragment.teacher_contact.model.StaffListApiModel
import com.mobatia.bisad.fragment.teacher_contact.model.StaffListModel
import com.mobatia.bisad.fragment.termdates.model.TermDatesApiModel
import com.mobatia.bisad.fragment.termdates.model.TermDatesListModel
import com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiDataModel
import com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiListModel
import com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
  /*************HOME_BANNER_IMAGE****************/
    @POST("api/v1/banner_images")
    @Headers("Content-Type: application/json")
    fun bannerimages(
        @Body  bannerModel: BannerModel,
        @Header("Authorization") token:String
    ):
     Call<ResponseBody>
    /*************SETTINGS USER DETAIL****************/
    @POST("api/v1/settings_userdetails")
    @Headers("Content-Type: application/json")
    fun settingsUserDetail(
        @Body  bannerModel: BannerModel,
        @Header("Authorization") token:String
    ):
     Call<ResponseBody>
    /*************COMMUNICATION_BANNER_IMAGE****************/
    @POST("api/v1/communication/banner_images")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun communication(
        @Header("Authorization") token:String
    ):
     Call<ResponseBody>

    /*************ACCESS TOKEN ****************/
    @POST("api/v1/user/token")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun access_token(
        @Header("authorization-user") usercode:String?
    ):
     Call<ResponseBody>

    /*************SIGN UP****************/
    @POST("api/v1/parent_signup")
    @FormUrlEncoded
    fun signup(
        @Field("email") email: String,
        @Field("devicetype") devicetype: Int,
        @Field("deviceid") deviceid: String
    ): Call<ResponseBody>
    /*************Forget Password****************/
    @POST("api/v1/forgot_password")
    @FormUrlEncoded
    fun forgetPassword(
        @Field("email") email: String
    ): Call<ResponseBody>

    /*************LOGIN****************/
    @POST("api/v1/login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("devicetype") devicetype: Int,
        @Field("deviceid") deviceid: String,
        @Field("fcm_id") fcmid: String
    ): Call<ResponseBody>

    /*************STUDENT_LIST****************/
    @POST("api/v1/student/list")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun studentList(
        @Header("Authorization") token:String
    ): Call<StudentListModel>
 /*************STAFF_LIST****************/
    @POST("api/v1/staff/list")
    @Headers("Content-Type: application/json")
    fun staffList(
     @Body  staffBody: StaffListApiModel,
     @Header("Authorization") token:String
    ): Call<StaffListModel>

    /*************STUDENT_INFO****************/
    @POST("api/v1/student/info")
    @Headers("Content-Type: application/json")
    fun studentInfo(
        @Body  studentbody: StudentInfoApiModel,
        @Header("Authorization") token:String
    ): Call<StudentInfoModel>

    /*************NOTIFICATION_LIST****************/
    @POST("api/v1/notification/list")
    @Headers("Content-Type: application/json")
    fun notificationList(
        @Body  messageListApiModel: MessageListApiModel,
        @Header("Authorization") token:String
    ): Call<MessageListModel>

      /*************Calendar List****************/
    @POST("api/v1/getcalendar")
    @Headers("Content-Type: application/json")
    fun calendarList(
        @Header("Authorization") token:String
    ): Call<CalendarModel>
    /*************ABSENCE List****************/
    @POST("api/v1/getcalendar_detail")
    @Headers("Content-Type: application/json")
    fun calendarDetail(
        @Body  calendarApi: CalendarApiModel,
        @Header("Authorization") token:String
    ): Call<CalendarListModel>
    /*************ABSENCE List****************/
    @POST("api/v1/leave/request")
    @Headers("Content-Type: application/json")
    fun absenceList(
        @Body  studentInfoModel: AbsenceLeaveApiModel,
        @Header("Authorization") token:String
    ): Call<AbsenceListModel>

    @POST("api/v1/social_media")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun socialMedia(
        @Header("Authorization") token:String
    ): Call<SocialMediaListModel>

/*Leave Request*/
   @POST("api/v1/request/leave")
   @Headers("Content-Type: application/json")
    fun leaveRequest(
    @Body  requestLeave: RequestAbsenceApiModel,
    @Header("Authorization") token:String
    ): Call<ResponseBody>

   /*SEND EMAIL TO STAFF*/
   @POST("api/v1/sendemail")
   @Headers("Content-Type: application/json")
    fun sendStaffMail(
    @Body  sendMail: SendStaffMailApiModel,
    @Header("Authorization") token:String
    ): Call<ResponseBody>

    /*************TERM_DATES LIST****************/
    @POST("api/v1/termdates")
    @Headers("Content-Type: application/json")
    fun termdates(
        @Header("Authorization") token:String
    ): Call<TermDatesDetailModel>

    @POST("api/v1/terms_of_service")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun termsOfService(
        @Header("Authorization") token:String
    ): Call<TermsOfServiceModel>


    @POST("api/v1/parent/logout")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun logout(
        @Header("Authorization") token:String
    ): Call<ResponseBody>

    /*CHANGE PASSWORD*/
    @POST("api/v1/changepassword")
    @Headers("Content-Type: application/json")
    fun changePassword(
        @Body  changePassword: ChangePasswordApiModel,
        @Header("Authorization") token:String
    ): Call<ResponseBody>

    /*************APPS LIST****************/
    @POST("api/v1/apps")
    @Headers("Content-Type: application/json")
    fun appsList(
        @Body  appsList: AppsApiModel,
        @Header("Authorization") token:String
    ): Call<AppsListModel>

    /*************TERM_DATES DETAIL****************/
    @POST("api/v1/termdate/details")
    @Headers("Content-Type: application/json")
    fun termDatesDetails(
        @Body  termDates: TermDatesDetailApiModel,
        @Header("Authorization") token:String
    ): Call<TermDatesDetailModel>


    /*************NEWSLETTER List****************/
    @POST("api/v1/newsletters")
    @Headers("Content-Type: application/json")
    fun newsletters(
        @Header("Authorization") token:String
    ): Call<NewsLetterListModel>

    /*************NEWSLETTER DETAIL****************/
    @POST("api/v1/newsletters/details")
    @Headers("Content-Type: application/json")
    fun newsletterdetail(
        @Body  newsLetterDetailApi: NewsLetterDetailApiModel,
        @Header("Authorization") token:String
    ): Call<NewsLetterDetailModel>

    /*************NOTIFICATION DETAIL****************/
    @POST("api/v1/notification/details")
    @Headers("Content-Type: application/json")
    fun notifictaionDetail(
        @Body  newsLetterDetailApi: MessageDetailApiModel,
        @Header("Authorization") token:String
    ): Call<MessageDetailModel>

    /*************TIME TABLE DATA****************/
    @POST("api/v1/timetable")
    @Headers("Content-Type: application/json")
    fun timetable(
        @Body  timeTableApi: TimeTableApiModel,
        @Header("Authorization") token:String
    ): Call<TimeTableApiDataModel>

    /*************TITLES_LIST****************/
    @POST("api/v1/titles")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun titlesList(
        @Header("Authorization") token:String
    ): Call<TilesListModel>
 /*************COUNTRY_LIST****************/
    @POST("api/v1/countries")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun countryList(
        @Header("Authorization") token:String
    ): Call<CountryListModel>
 /*************RELATIONSHIP_LIST****************/
    @POST("api/v1/contact_types")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun relationshipList(
        @Header("Authorization") token:String
    ): Call<RelationShipListModel>

    /*************DATA_COLLECTION_DETAIL****************/
    @POST("api/v1/data_collection_details")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun dataCollectionDetail(
        @Header("Authorization") token:String
    ): Call<DataCollectionModel>
    /*************Contact Us****************/
    @POST("api/v1/contact_us")
    fun contactus(
        @Header("Authorization") token:String
    ): Call<ContactusModel>

    /*************Attendance List****************/
    @POST("api/v1/attendance_record")
    @Headers("Content-Type: application/json")
    fun attendanceList(
        @Body attendanceListModel: AttendanceApiModel,
        @Header("Authorization") token:String
    ): Call<AttendanceListModel>

    /*************Report List****************/
    @POST("api/v1/progressreport")
    @Headers("Content-Type: application/json")
    fun reportList(
        @Body reportListModel: ReportApiModel,
        @Header("Authorization") token:String
    ): Call<ReportListModel>
    /*Leave Request*/
    @POST("api/v1/submit_datacollection")
    @Headers("Content-Type: application/json")
    fun dataCollectionSubmittion(
        @Body  dataLeave: DataCollectionSubmissionModel,
        @Header("Authorization") token:String
    ): Call<ResponseBody>
 @POST("api/v1/trigger_user")
    @Headers("Content-Type: application/json")
    fun triggerUser(
        @Body  trigger: TriggerUSer,
        @Header("Authorization") token:String
    ): Call<ResponseBody>
    /*************Report List****************/
    @POST("api/v1/curriculm_guides")
    @Headers("Content-Type: application/json")
    fun curriculumList(
        @Body reportListModel: CurriculumStudentApiModel,
        @Header("Authorization") token:String
    ): Call<CuriculumListModel>
}