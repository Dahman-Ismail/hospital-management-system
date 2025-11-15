// Define all possible roles
export type UserRole = "ADMIN" | "PATIENT";

// Base user interface
export interface BaseUser {
  id: number;
  name: string;
  email: string;
  role: UserRole;
}

// Optionally extend for specific roles
export interface AdminUser extends BaseUser {
  role: "ADMIN";
  // Admin-specific fields if any
}

export interface PatientUser extends BaseUser {
  role: "PATIENT";
  medicalRecordId?: string;
}

// Union type for any user
export type AnyUser = AdminUser | PatientUser;
