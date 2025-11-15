import useSWR from "swr";
import { fetcher } from "./fetcher";

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
}

export interface ApiResult<T> {
  data?: T;
  error?: string;
  isError: boolean;
  isLoading: boolean;
}

// ---------- GET wrapper ----------
export function useApiGet<T>(key: string) {
  const { data, error, isLoading, isValidating } = useSWR<T>(key, fetcher);

  return {
    data,
    error:
      error instanceof Error
        ? error.message
        : error
        ? String(error)
        : undefined,
    isLoading,
    isValidating,
    isError: !!error,
  };
}

// ---------- POST wrapper ----------
export async function apiPost<T, B>(
  key: string,
  body: B
): Promise<ApiResult<T>> {
  let isLoading = true;

  try {
    const data = await fetcher<T>(key, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    isLoading = false;
    return { data, isError: false, isLoading };
  } catch (err: unknown) {
    let message = "Something went wrong";
    if (err instanceof Error) message = err.message;

    isLoading = false;
    return { error: message, isError: true, isLoading };
  }
}

// ---------- DELETE wrapper ----------
export async function apiDelete<T>(key: string): Promise<ApiResult<T>> {
  let isLoading = true;

  try {
    const data = await fetcher<T>(key, { method: "DELETE" });

    isLoading = false;
    return { data, isError: false, isLoading };
  } catch (err: unknown) {
    let message = "Something went wrong";
    if (err instanceof Error) message = err.message;

    isLoading = false;
    return { error: message, isError: true, isLoading };
  }
}