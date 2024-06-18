import {
  NativeModulesProxy,
  EventEmitter,
  Subscription,
} from "expo-modules-core";
import { Platform } from "react-native";

import ExpoOtpModule from "./ExpoOtpModule";

export { useOneTimePassword } from "./useOneTimePassword";

const emitter = new EventEmitter(ExpoOtpModule ?? NativeModulesProxy.ExpoOtp);

const isAndroid = Platform.OS === "android";

export const getHash = () => {
  return isAndroid && ExpoOtpModule.getHash();
};

export const startAndListener = async (
  listener: (value: string) => void,
): Promise<Subscription> => {
  if (isAndroid) {
    await ExpoOtpModule.startSmsRetrieverAsync();
    return emitter.addListener("OptEventSms", listener);
  }
  return {} as Subscription;
};

export const removeListener = () => {
  return isAndroid && emitter.removeAllListeners("OptEventSms");
};
