import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoOtp.web.ts
// and on native platforms to ExpoOtp.ts
import ExpoOtpModule from './ExpoOtpModule';
import ExpoOtpView from './ExpoOtpView';
import { ChangeEventPayload, ExpoOtpViewProps } from './ExpoOtp.types';

// Get the native constant value.
export const PI = ExpoOtpModule.PI;

export function hello(): string {
  return ExpoOtpModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoOtpModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoOtpModule ?? NativeModulesProxy.ExpoOtp);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoOtpView, ExpoOtpViewProps, ChangeEventPayload };
