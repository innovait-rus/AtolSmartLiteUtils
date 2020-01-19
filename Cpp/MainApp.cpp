
#include <wchar.h>
#include <thread>
#include "MainApp.h"
#include "ConversionWchar.h"
#include "AddInNative.h"

MainApp::MainApp() : cc(nullptr), obj(nullptr)
{
}

MainApp::~MainApp()
{
	if (obj)
	{
        stopEventsWatch(); // call to unregister broadcast receiver
		JNIEnv *env = getJniEnv();
		env->DeleteGlobalRef(obj);
		env->DeleteGlobalRef(cc);
	}
}

void MainApp::Initialize(IAddInDefBaseEx* cnn)
{
	if (!obj)
	{
		IAndroidComponentHelper* helper = (IAndroidComponentHelper*)cnn->GetInterface(eIAndroidComponentHelper);
		if (helper)
		{
			WCHAR_T* className = nullptr;
			convToShortWchar(&className, L"org.innovait.atolsmartliteutils.MainApp");
			jclass ccloc = helper->FindClass(className);
			delete[] className;
			className = nullptr;
			if (ccloc)
			{
				JNIEnv* env = getJniEnv();
				cc = static_cast<jclass>(env->NewGlobalRef(ccloc));
				env->DeleteLocalRef(ccloc);
				jobject activity = helper->GetActivity();
				// call of constructor for java class
				jmethodID methID = env->GetMethodID(cc, "<init>", "(Landroid/app/Activity;J)V");
				jobject objloc = env->NewObject(cc, methID, activity, (jlong)cnn);
				obj = static_cast<jobject>(env->NewGlobalRef(objloc));
				env->DeleteLocalRef(objloc);
				methID = env->GetMethodID(cc, "show", "()V");
				env->CallVoidMethod(obj, methID);
				env->DeleteLocalRef(activity);
			}
		}
	}
}

void MainApp::startEventsWatch() const
{

	if (obj)
	{

		JNIEnv* env = getJniEnv();
		jmethodID methID = env->GetMethodID(cc, "startEventsWatch", "()V");
		env->CallVoidMethod(obj, methID);
	}
}

void MainApp::stopEventsWatch() const
{
	if (obj)
	{
		JNIEnv* env = getJniEnv();
		jmethodID methID = env->GetMethodID(cc, "stopEventsWatch", "()V");
		env->CallVoidMethod(obj, methID);
	}
}

static const wchar_t g_EventSource[] = L"InnovaIT-EventReceived";

static WcharWrapper s_EventSource(g_EventSource);


std::wstring ToWStringJni(jstring jstr)
{
	std::wstring ret;
	if (jstr)
	{
		JNIEnv* env = getJniEnv();
		const jchar* jChars = env->GetStringChars(jstr, NULL);
		jsize jLen = env->GetStringLength(jstr);
		ret.assign(jChars, jChars + jLen);
		env->ReleaseStringChars(jstr, jChars);
	}
	return ret;
}


extern "C" JNIEXPORT void JNICALL Java_org_innovait_atolsmartliteutils_MainApp_OnBroadcastReceive(JNIEnv* env, jclass jClass, jlong pObject, jstring j_event, jstring j_type, jstring j_data) {
	IAddInDefBaseEx *pAddIn = (IAddInDefBaseEx *) pObject;
	if (pAddIn != nullptr) {
		
		std::wstring ws_event =ToWStringJni(j_event);
		std::wstring ws_type = ToWStringJni(j_type);
		std::wstring ws_data = ToWStringJni(j_data);

		std::wstring obj_data{};

		obj_data = L"{\"type\": \"" + ws_type + L"\", \"data\": \"" + ws_data + L"\"}";	

		WcharWrapper wdata((wchar_t*)obj_data.c_str());
		WcharWrapper wmsg((wchar_t*)ws_event.c_str());

		pAddIn->ExternalEvent(s_EventSource, wmsg, wdata);
	}
}

void MainApp::setCC(jclass _cc) {
    cc = _cc;
}

void MainApp::setOBJ(jobject _obj) {
    obj= _obj;
}
