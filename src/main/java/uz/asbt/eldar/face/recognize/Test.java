package uz.asbt.eldar.face.recognize;

import Luxand.FSDK;
import com.sun.jna.*;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

public class Test {

    private interface IFaceSDK extends Library {

        int FSDK_ActivateLibrary(String var1);

        int FSDK_SetNumThreads(int var1);

        int FSDK_GetNumThreads(IntByReference var1);

        int FSDK_Initialize(String var1);

        int FSDK_GetHardware_ID(Pointer var1);

        int FSDK_GetLicenseInfo(Pointer var1);

        int FSDK_Finalize();

        int FSDK_DetectFace(int var1, FSDK.TFacePosition.ByReference var2);

        int FSDK_DetectMultipleFaces(int var1, IntByReference var2, Pointer var3, int var4);

        int FSDK_DetectEyes(int var1, FSDK.FSDK_Features.ByReference var2);

        int FSDK_DetectEyesInRegion(int var1, FSDK.TFacePosition var2, FSDK.FSDK_Features.ByReference var3);

        int FSDK_DetectFacialFeatures(int var1, FSDK.FSDK_Features.ByReference var2);

        int FSDK_DetectFacialFeaturesInRegion(int var1, FSDK.TFacePosition var2, FSDK.FSDK_Features.ByReference var3);

        int FSDK_SetFaceDetectionParameters(byte var1, byte var2, int var3);

        int FSDK_SetFaceDetectionThreshold(int var1);

        int FSDK_CreateEmptyImage(IntByReference var1);

        int FSDK_LoadImageFromFile(IntByReference var1, String var2);

        int FSDK_LoadImageFromFileWithAlpha(IntByReference var1, String var2);

        int FSDK_LoadImageFromFileW(IntByReference var1, WString var2);

        int FSDK_LoadImageFromFileWithAlphaW(IntByReference var1, WString var2);

        int FSDK_LoadImageFromBuffer(IntByReference var1, byte[] var2, int var3, int var4, int var5, int var6);

        int FSDK_FreeImage(int var1);

        int FSDK_SaveImageToFile(int var1, String var2);

        int FSDK_SaveImageToFileW(int var1, WString var2);

        int FSDK_GetImageBufferSize(int var1, IntByReference var2, int var3);

        int FSDK_SaveImageToBuffer(int var1, byte[] var2, int var3);

        int FSDK_SetJpegCompressionQuality(int var1);

        int FSDK_CopyImage(int var1, int var2);

        int FSDK_ResizeImage(int var1, double var2, int var4);

        int FSDK_RotateImage90(int var1, int var2, int var3);

        int FSDK_RotateImage(int var1, double var2, int var4);

        int FSDK_RotateImageCenter(int var1, double var2, double var4, double var6, int var8);

        int FSDK_CopyRect(int var1, int var2, int var3, int var4, int var5, int var6);

        int FSDK_CopyRectReplicateBorder(int var1, int var2, int var3, int var4, int var5, int var6);

        int FSDK_MirrorImage(int var1, byte var2);

        int FSDK_GetImageWidth(int var1, IntByReference var2);

        int FSDK_GetImageHeight(int var1, IntByReference var2);

        int FSDK_ExtractFaceImage(int var1, FSDK.FSDK_Features.ByReference var2, int var3, int var4, IntByReference var5, FSDK.FSDK_Features.ByReference var6);

        int FSDK_GetFaceTemplate(int var1, FSDK.FSDK_FaceTemplate.ByReference var2);

        int FSDK_MatchFaces(FSDK.FSDK_FaceTemplate.ByReference var1, FSDK.FSDK_FaceTemplate.ByReference var2, FloatByReference var3);

        int FSDK_GetFaceTemplateInRegion(int var1, FSDK.TFacePosition var2, FSDK.FSDK_FaceTemplate.ByReference var3);

        int FSDK_GetFaceTemplateUsingFeatures(int var1, FSDK.FSDK_Features var2, FSDK.FSDK_FaceTemplate.ByReference var3);

        int FSDK_GetFaceTemplateUsingEyes(int var1, FSDK.FSDK_Features var2, FSDK.FSDK_FaceTemplate.ByReference var3);

        int FSDK_GetMatchingThresholdAtFAR(float var1, FloatByReference var2);

        int FSDK_GetMatchingThresholdAtFRR(float var1, FloatByReference var2);

        int FSDK_GetDetectedFaceConfidence(IntByReference var1);

        int FSDK_CreateTracker(IntByReference var1);

        int FSDK_FreeTracker(int var1);

        int FSDK_ClearTracker(int var1);

        int FSDK_SetTrackerParameter(int var1, String var2, String var3);

        int FSDK_SetTrackerMultipleParameters(int var1, String var2, IntByReference var3);

        int FSDK_GetTrackerParameter(int var1, String var2, Pointer var3, long var4);

        int FSDK_FeedFrame(int var1, long var2, int var4, LongByReference var5, Pointer var6, long var7);

        int FSDK_GetTrackerEyes(int var1, long var2, long var4, FSDK.FSDK_Features.ByReference var6);

        int FSDK_GetTrackerFacialFeatures(int var1, long var2, long var4, FSDK.FSDK_Features.ByReference var6);

        int FSDK_GetTrackerFacePosition(int var1, long var2, long var4, FSDK.TFacePosition.ByReference var6);

        int FSDK_LockID(int var1, long var2);

        int FSDK_UnlockID(int var1, long var2);

        int FSDK_GetName(int var1, long var2, Pointer var4, long var5);

        int FSDK_SetName(int var1, long var2, String var4);

        int FSDK_GetIDReassignment(int var1, long var2, LongByReference var4);

        int FSDK_GetSimilarIDCount(int var1, long var2, LongByReference var4);

        int FSDK_GetSimilarIDList(int var1, long var2, Pointer var4, long var5);

        int FSDK_GetAllNames(int var1, long var2, Pointer var4, long var5);

        int FSDK_LoadTrackerMemoryFromFile(IntByReference var1, String var2);

        int FSDK_SaveTrackerMemoryToFile(int var1, String var2);

        int FSDK_LoadTrackerMemoryFromBuffer(IntByReference var1, byte[] var2);

        int FSDK_GetTrackerMemoryBufferSize(int var1, LongByReference var2);

        int FSDK_SaveTrackerMemoryToBuffer(int var1, byte[] var2, long var3);

        int FSDK_GetTrackerFacialAttribute(int var1, long var2, long var4, String var6, Pointer var7, long var8);

        int FSDK_DetectFacialAttributeUsingFeatures(int var1, FSDK.FSDK_Features var2, String var3, Pointer var4, long var5);

        int FSDK_GetValueConfidence(String var1, String var2, FloatByReference var3);
    }

}
