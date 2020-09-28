/* tslint:disable:prefer-const */
let EventSourceMap: { [key: string]: EventSource } = {};

export function getEventSource(url, options: EventSourceInit = {}): EventSource {
  if (!EventSourceMap[url]) {
    EventSourceMap[url] = new EventSource(url, options);
  }
  return EventSourceMap[url];
}

export function closeEventSource(url): void {
  if (EventSourceMap[url]) {
    EventSourceMap[url].close();
    delete EventSourceMap[url];
  }
}
