#pragma once

#include "AddInDefBase.h"
#include "IAndroidComponentHelper.h"
#include "jnienv.h"
#include "IMemoryManager.h"
#include <string>

/* Wrapper calling class LockState from java build org.ripreal.androidutils */

class MainApp
{
private:

	jclass cc;
	jobject obj;
	std::wstring jstring2wstring(JNIEnv* jenv, jstring aStr);
	std::wstring ToWStringJni(jstring jstr);

public:

	MainApp();
	~MainApp();

	void setCC(jclass _cc);
	void setOBJ(jobject _obj);
	void Initialize(IAddInDefBaseEx*);
	void startEventsWatch() const;
	void stopEventsWatch() const;
};