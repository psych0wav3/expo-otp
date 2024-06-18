import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoOtpViewProps } from './ExpoOtp.types';

const NativeView: React.ComponentType<ExpoOtpViewProps> =
  requireNativeViewManager('ExpoOtp');

export default function ExpoOtpView(props: ExpoOtpViewProps) {
  return <NativeView {...props} />;
}
