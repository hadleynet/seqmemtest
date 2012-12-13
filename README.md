# seqmemtest

Command line wrapper for the sequence memoizer library.

## Installation

    $ lein run

## Usage

    $ java -jar seqmemtest-0.1.0-standalone.jar [args]

## Options

    -h, --no-help, --help  false    Show help
    -s, --sequence         []       The sequence to memoize
    -g, --generated        5        The number of samples to generate
    -c, --context          []       The context from which to begin generating samples
    -i, --iterations       100      The number of sampling iterations when generating the model
 
## License

The MITRE Corporation

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 except in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the
License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
either express or implied. See the License for the specific language governing permissions 
and limitations under the License.