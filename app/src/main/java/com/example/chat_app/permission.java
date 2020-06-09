
/**
 * private void tedPermission() {
 * PermissionListener permissionListener = new PermissionListener() {
 *
 * @Override public void onPermissionGranted() {
 * isPermission = true;
 * }
 * @Override public void onPermissionDenied(ArrayList<String> deniedPermissions) {
 * isPermission = false;
 * }
 * };
 * <p>
 * TedPermission.with(this)
 * .setPermissionListener(permissionListener)
 * .setRationaleMessage(getResources().getString(R.string.permission_2))
 * .setDeniedMessage(getResources().getString(R.string.permission_1))
 * .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
 * .check();
 * }
 **/