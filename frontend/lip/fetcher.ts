export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: string;
}

/**
 * Generic SWR fetcher for your API
 */
export const fetcher = async <T>(
  url: string,
  init?: RequestInit
): Promise<T> => {
  const res = await fetch(url, init);
  const json = (await res.json()) as ApiResponse<T>;

  if (!res.ok || !json.success || json.data === undefined) {
    throw new Error(json.error ?? `Request failed with status ${res.status}`);
  }

  return json.data;
};
